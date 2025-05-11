package com.dut.jfix_be.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dut.jfix_be.service.FuriganaService;
import com.dut.jfix_be.service.SpeechToTextService;
import com.dut.jfix_be.util.diff_match_patch;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpeechToTextServiceImpl implements SpeechToTextService {
    @Value("${ai-service.url}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final FuriganaService furiganaService;

    @Override
    public String speechToText(String base64Audio, String language) {
        Map<String, Object> request = new HashMap<>();
        request.put("audio_data", base64Audio);
        if (language != null && !language.isEmpty()) {
            request.put("language", language);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(aiServiceUrl + "/speech-to-text", entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object text = response.getBody().get("text");
            return text != null ? text.toString() : null;
        }
        return null;
    }

    @Override
    public Map<String, Object> checkSpeech(String audioData, String userJapanese, String language) {
        Map<String, Object> result = new HashMap<>();
        String text = speechToText(audioData, language);
        if (text == null) {
            result.put("status", "fail");
            result.put("message", "Speech-to-text API error");
            return result;
        }
        // Chuyển cả text (API) và userJapanese về hiragana
        String hiraApi = ((com.dut.jfix_be.service.impl.FuriganaServiceImpl)furiganaService).toHiragana(text);
        String hiraUser = ((com.dut.jfix_be.service.impl.FuriganaServiceImpl)furiganaService).toHiragana(userJapanese);
        // Loại bỏ dấu câu phổ biến, dấu * và khoảng trắng
        String hiraApiNoPunct = hiraApi.replaceAll("[、。！？「」（）\\[\\]{}.,!?\\-\\s*]", "");
        String hiraUserNoPunct = hiraUser.replaceAll("[、。！？「」（）\\[\\]{}.,!?\\-\\s*]", "");

        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(hiraApiNoPunct, hiraUserNoPunct);
        dmp.diff_cleanupSemantic(diffs);

        List<Map<String, Object>> wrongJapanese = new ArrayList<>();
        int apiPos = 0, userPos = 0;
        StringBuilder apiChunk = new StringBuilder();
        StringBuilder userChunk = new StringBuilder();
        Integer apiStart = null, userStart = null, apiEnd = null, userEnd = null;

        for (diff_match_patch.Diff diff : diffs) {
            if (diff.operation == diff_match_patch.Operation.EQUAL) {
                if (apiChunk.length() > 0 || userChunk.length() > 0) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("apiStart", apiStart);
                    detail.put("apiEnd", apiEnd);
                    detail.put("userStart", userStart);
                    detail.put("userEnd", userEnd);
                    detail.put("api", apiChunk.toString().replaceAll("[*\\s]", ""));
                    detail.put("user", userChunk.toString().replaceAll("[*\\s]", ""));
                    wrongJapanese.add(detail);
                    apiChunk.setLength(0);
                    userChunk.setLength(0);
                    apiStart = userStart = apiEnd = userEnd = null;
                }
                apiPos += diff.text.length();
                userPos += diff.text.length();
            } else {
                if (apiChunk.length() == 0 && userChunk.length() == 0) {
                    apiStart = apiPos;
                    userStart = userPos;
                }
                if (diff.operation == diff_match_patch.Operation.DELETE) {
                    apiChunk.append(diff.text);
                    apiEnd = apiPos + diff.text.length() - 1;
                    apiPos += diff.text.length();
                } else if (diff.operation == diff_match_patch.Operation.INSERT) {
                    userChunk.append(diff.text);
                    userEnd = userPos + diff.text.length() - 1;
                    userPos += diff.text.length();
                }
            }
        }
        if (apiChunk.length() > 0 || userChunk.length() > 0) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("apiStart", apiStart);
            detail.put("apiEnd", apiEnd);
            detail.put("userStart", userStart);
            detail.put("userEnd", userEnd);
            detail.put("api", apiChunk.toString().replaceAll("[*\\s]", ""));
            detail.put("user", userChunk.toString().replaceAll("[*\\s]", ""));
            wrongJapanese.add(detail);
        }
        boolean match = wrongJapanese.isEmpty();
        int total = Math.max(hiraApiNoPunct.length(), hiraUserNoPunct.length());
        int correct = total - wrongJapanese.size();
        double accuracy = total == 0 ? 0.0 : (correct * 100.0) / total;
        result.put("status", "success");
        result.put("text", text);
        result.put("hiraApi", hiraApi);
        result.put("hiraUser", hiraUser);
        result.put("match", match);
        result.put("wrongJapanese", wrongJapanese);
        result.put("accuracy", accuracy);
        return result;
    }
} 