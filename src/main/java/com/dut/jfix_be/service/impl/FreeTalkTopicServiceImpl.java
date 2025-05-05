package com.dut.jfix_be.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.FreeTalkTopicRequest;
import com.dut.jfix_be.dto.response.FreeTalkTopicAdminResponse;
import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.FreeTalkTopicRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.FreeTalkTopicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeTalkTopicServiceImpl implements FreeTalkTopicService {
    private final FreeTalkTopicRepository freeTalkTopicRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public DataWithPageResponse<FreeTalkTopicAdminResponse> getAllFreeTalkTopicsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<FreeTalkTopic> topics = freeTalkTopicRepository.findAll();
        List<FreeTalkTopicAdminResponse> filtered = topics.stream()
                .filter(t -> (keyword == null || t.getJapaneseText().toLowerCase().contains(keyword.toLowerCase()) || (t.getVietnameseText() != null && t.getVietnameseText().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(t -> (level == null || t.getLevel() == level))
                .sorted((t1, t2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "japaneseText": cmp = t1.getJapaneseText().compareToIgnoreCase(t2.getJapaneseText()); break;
                        case "level": cmp = t1.getLevel().name().compareTo(t2.getLevel().name()); break;
                        case "id": default: cmp = t1.getId().compareTo(t2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(FreeTalkTopicAdminResponse::fromFreeTalkTopic)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<FreeTalkTopicAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<FreeTalkTopicAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public FreeTalkTopicAdminResponse getFreeTalkTopicDetailForAdmin(Integer id) {
        FreeTalkTopic t = freeTalkTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.free.talk.topic.not.found", id));
        return FreeTalkTopicAdminResponse.fromFreeTalkTopic(t);
    }

    @Override
    public FreeTalkTopicAdminResponse createFreeTalkTopicForAdmin(FreeTalkTopicRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        FreeTalkTopic t = new FreeTalkTopic();
        t.setJapaneseText(request.getJapaneseText());
        t.setVietnameseText(request.getVietnameseText());
        t.setLevel(request.getLevel());
        t.setConversationPrompt(request.getConversationPrompt());
        t.setSampleAnswerVietnamese(request.getSampleAnswerVietnamese());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            t.setAudioUrl(audioUrl);
        }
        t.setCreateDate(java.time.LocalDateTime.now());
        t.setCreateBy(adminUsername);
        freeTalkTopicRepository.save(t);
        return FreeTalkTopicAdminResponse.fromFreeTalkTopic(t);
    }

    @Override
    public FreeTalkTopicAdminResponse updateFreeTalkTopicForAdmin(Integer id, FreeTalkTopicRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        FreeTalkTopic t = freeTalkTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.free.talk.topic.not.found", id));
        t.setJapaneseText(request.getJapaneseText());
        t.setVietnameseText(request.getVietnameseText());
        t.setLevel(request.getLevel());
        t.setConversationPrompt(request.getConversationPrompt());
        t.setSampleAnswerVietnamese(request.getSampleAnswerVietnamese());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String oldAudioUrl = t.getAudioUrl();
            if (oldAudioUrl != null && !oldAudioUrl.isEmpty() && oldAudioUrl.contains("cloudinary")) {
                cloudinaryService.deleteAudio(oldAudioUrl);
            }
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            t.setAudioUrl(audioUrl);
        }
        t.setUpdateDate(java.time.LocalDateTime.now());
        t.setUpdateBy(adminUsername);
        freeTalkTopicRepository.save(t);
        return FreeTalkTopicAdminResponse.fromFreeTalkTopic(t);
    }

    @Override
    public void deleteFreeTalkTopicForAdmin(Integer id) {
        FreeTalkTopic t = freeTalkTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.free.talk.topic.not.found", id));
        freeTalkTopicRepository.delete(t);
    }
}