package com.dut.jfix_be.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.SpeakingQuestionRequest;
import com.dut.jfix_be.dto.response.SpeakingQuestionAdminResponse;
import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.SpeakingQuestionRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.SpeakingQuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpeakingQuestionServiceImpl implements SpeakingQuestionService {
    private final SpeakingQuestionRepository speakingQuestionRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public DataWithPageResponse<SpeakingQuestionAdminResponse> getAllSpeakingQuestionsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<SpeakingQuestion> questions = speakingQuestionRepository.findAll();
        List<SpeakingQuestionAdminResponse> filtered = questions.stream()
                .filter(q -> (keyword == null || q.getJapaneseText().toLowerCase().contains(keyword.toLowerCase()) || (q.getVietnameseText() != null && q.getVietnameseText().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(q -> (level == null || q.getLevel() == level))
                .sorted((q1, q2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "japaneseText": cmp = q1.getJapaneseText().compareToIgnoreCase(q2.getJapaneseText()); break;
                        case "level": cmp = q1.getLevel().name().compareTo(q2.getLevel().name()); break;
                        case "id": default: cmp = q1.getId().compareTo(q2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(SpeakingQuestionAdminResponse::fromSpeakingQuestion)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<SpeakingQuestionAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<SpeakingQuestionAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public SpeakingQuestionAdminResponse getSpeakingQuestionDetailForAdmin(Integer id) {
        SpeakingQuestion q = speakingQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.speaking.question.not.found", id));
        return SpeakingQuestionAdminResponse.fromSpeakingQuestion(q);
    }

    @Override
    public SpeakingQuestionAdminResponse createSpeakingQuestionForAdmin(SpeakingQuestionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        SpeakingQuestion q = new SpeakingQuestion();
        q.setJapaneseText(request.getJapaneseText());
        q.setVietnameseText(request.getVietnameseText());
        q.setLevel(request.getLevel());
        q.setSampleAnswerJapanese(request.getSampleAnswerJapanese());
        q.setSampleAnswerVietnamese(request.getSampleAnswerVietnamese());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            q.setAudioUrl(audioUrl);
        }
        q.setCreateDate(java.time.LocalDateTime.now());
        q.setCreateBy(adminUsername);
        speakingQuestionRepository.save(q);
        return SpeakingQuestionAdminResponse.fromSpeakingQuestion(q);
    }

    @Override
    public SpeakingQuestionAdminResponse updateSpeakingQuestionForAdmin(Integer id, SpeakingQuestionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        SpeakingQuestion q = speakingQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.speaking.question.not.found", id));
        q.setJapaneseText(request.getJapaneseText());
        q.setVietnameseText(request.getVietnameseText());
        q.setLevel(request.getLevel());
        q.setSampleAnswerJapanese(request.getSampleAnswerJapanese());
        q.setSampleAnswerVietnamese(request.getSampleAnswerVietnamese());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String oldAudioUrl = q.getAudioUrl();
            if (oldAudioUrl != null && !oldAudioUrl.isEmpty() && oldAudioUrl.contains("cloudinary")) {
                cloudinaryService.deleteAudio(oldAudioUrl);
            }
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            q.setAudioUrl(audioUrl);
        }
        q.setUpdateDate(java.time.LocalDateTime.now());
        q.setUpdateBy(adminUsername);
        speakingQuestionRepository.save(q);
        return SpeakingQuestionAdminResponse.fromSpeakingQuestion(q);
    }

    @Override
    public void deleteSpeakingQuestionForAdmin(Integer id) {
        SpeakingQuestion q = speakingQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.speaking.question.not.found", id));
        speakingQuestionRepository.delete(q);
    }
}