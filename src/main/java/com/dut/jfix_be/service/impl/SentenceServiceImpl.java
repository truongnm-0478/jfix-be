package com.dut.jfix_be.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.SentenceRequest;
import com.dut.jfix_be.dto.response.SentenceAdminResponse;
import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.SentenceRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.SentenceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SentenceServiceImpl implements SentenceService {
    private final SentenceRepository sentenceRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public DataWithPageResponse<SentenceAdminResponse> getAllSentencesForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<Sentence> sentences = sentenceRepository.findAll();
        List<SentenceAdminResponse> filtered = sentences.stream()
                .filter(s -> (keyword == null || s.getJapaneseText().toLowerCase().contains(keyword.toLowerCase()) || (s.getVietnameseText() != null && s.getVietnameseText().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(s -> (level == null || s.getLevel() == level))
                .sorted((s1, s2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "japaneseText": cmp = s1.getJapaneseText().compareToIgnoreCase(s2.getJapaneseText()); break;
                        case "level": cmp = s1.getLevel().name().compareTo(s2.getLevel().name()); break;
                        case "id": default: cmp = s1.getId().compareTo(s2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(SentenceAdminResponse::fromSentence)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<SentenceAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<SentenceAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public SentenceAdminResponse getSentenceDetailForAdmin(Integer id) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.sentence.not.found", id));
        return SentenceAdminResponse.fromSentence(sentence);
    }

    @Override
    public SentenceAdminResponse createSentenceForAdmin(SentenceRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Sentence sentence = new Sentence();
        sentence.setJapaneseText(request.getJapaneseText());
        sentence.setVietnameseText(request.getVietnameseText());
        sentence.setLevel(request.getLevel());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            sentence.setAudioUrl(audioUrl);
        }
        sentence.setCreateDate(java.time.LocalDateTime.now());
        sentence.setCreateBy(adminUsername);
        sentenceRepository.save(sentence);
        return SentenceAdminResponse.fromSentence(sentence);
    }

    @Override
    public SentenceAdminResponse updateSentenceForAdmin(Integer id, SentenceRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.sentence.not.found", id));
        sentence.setJapaneseText(request.getJapaneseText());
        sentence.setVietnameseText(request.getVietnameseText());
        sentence.setLevel(request.getLevel());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String oldAudioUrl = sentence.getAudioUrl();
            if (oldAudioUrl != null && !oldAudioUrl.isEmpty() && oldAudioUrl.contains("cloudinary")) {
                cloudinaryService.deleteAudio(oldAudioUrl);
            }
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            sentence.setAudioUrl(audioUrl);
        }
        sentence.setUpdateDate(java.time.LocalDateTime.now());
        sentence.setUpdateBy(adminUsername);
        sentenceRepository.save(sentence);
        return SentenceAdminResponse.fromSentence(sentence);
    }

    @Override
    public void deleteSentenceForAdmin(Integer id) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.sentence.not.found", id));
        sentenceRepository.delete(sentence);
    }
}