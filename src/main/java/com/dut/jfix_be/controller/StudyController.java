package com.dut.jfix_be.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.request.StudyLogRequest;
import com.dut.jfix_be.dto.response.ReviewDeckResponse;
import com.dut.jfix_be.service.SpeechToTextService;
import com.dut.jfix_be.service.StudyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final SpeechToTextService speechToTextService;

    @GetMapping("/vocabulary")
    public ResponseEntity<List<ReviewDeckResponse>> getVocabularyCards(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getVocabularyCards(date));
    }

    @GetMapping("/grammar")
    public ResponseEntity<List<ReviewDeckResponse>> getGrammarCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getGrammarCards(date));
    }

    @GetMapping("/paragraph")
    public ResponseEntity<List<ReviewDeckResponse>> getParagraphCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getParagraphCards(date));
    }

    @GetMapping("/sentence")
    public ResponseEntity<List<ReviewDeckResponse>> getSentenceCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getSentenceCards(date));
    }

    @GetMapping("/speaking-question")
    public ResponseEntity<List<ReviewDeckResponse>> getSpeakingQuestionCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getSpeakingQuestionCards(date));
    }

    @GetMapping("/free-talk-topic")
    public ResponseEntity<List<ReviewDeckResponse>> getFreeTalkTopicCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(studyService.getFreeTalkTopicCards(date));
    }

    @PutMapping("/vocabulary")
    public ResponseEntity<List<ReviewDeckResponse>> updateVocabularyStudyLog(@RequestBody StudyLogRequest request) {
        studyService.updateVocabularyStudyLog(request);
        LocalDate today = LocalDate.now();
        List<ReviewDeckResponse> cards = studyService.getVocabularyCards(today);
        cards.forEach(deck -> deck.getCards().sort((a, b) -> a.getReviewDate().compareTo(b.getReviewDate())));
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/grammar")
    public ResponseEntity<List<ReviewDeckResponse>> updateGrammarStudyLog(@RequestBody StudyLogRequest request) {
        studyService.updateGrammarStudyLog(request);
        LocalDate today = LocalDate.now();
        List<ReviewDeckResponse> cards = studyService.getGrammarCards(today);
        cards.forEach(deck -> deck.getCards().sort((a, b) -> a.getReviewDate().compareTo(b.getReviewDate())));
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/paragraph")
    public ResponseEntity<List<ReviewDeckResponse>> updateParagraphStudyLog(@RequestBody StudyLogRequest request) {
        return ResponseEntity.ok(studyService.updateAndReturnParagraphDecks(request));
    }

    @PutMapping("/sentence")
    public ResponseEntity<List<ReviewDeckResponse>> updateSentenceStudyLog(@RequestBody StudyLogRequest request) {
        return ResponseEntity.ok(studyService.updateAndReturnSentenceDecks(request));
    }

    @PutMapping("/speaking-question")
    public ResponseEntity<List<ReviewDeckResponse>> updateSpeakingQuestionStudyLog(@RequestBody StudyLogRequest request) {
        return ResponseEntity.ok(studyService.updateAndReturnSpeakingQuestionDecks(request));
    }

    @PutMapping("/free-talk-topic")
    public ResponseEntity<List<ReviewDeckResponse>> updateFreeTalkTopicStudyLog(@RequestBody StudyLogRequest request) {
        return ResponseEntity.ok(studyService.updateAndReturnFreeTalkTopicDecks(request));
    }

    @PostMapping("/speech-to-text")
    public ResponseEntity<?> speechToTextCheck(@RequestBody Map<String, String> req) {
        String audioData = req.get("audio_data");
        String userRomaji = req.get("user_romaji");
        String language = req.getOrDefault("language", "ja-JP");
        Map<String, Object> result = speechToTextService.checkSpeech(audioData, userRomaji, language);
        if ("fail".equals(result.get("status"))) {
            return ResponseEntity.status(502).body(result);
        }
        return ResponseEntity.ok(result);
    }
} 