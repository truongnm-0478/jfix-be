package com.dut.jfix_be.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.ParagraphRequest;
import com.dut.jfix_be.dto.response.ParagraphAdminResponse;
import com.dut.jfix_be.dto.response.ParagraphResponse;
import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.ParagraphRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.ParagraphService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParagraphServiceImpl implements ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ParagraphResponse findById(Integer id) {
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.paragraph.not.found", id));
        return ParagraphResponse.fromParagraph(paragraph);
    }

    @Override
    public DataWithPageResponse<ParagraphResponse> getAllParagraphs(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<Paragraph> paragraphs = paragraphRepository.findAll();
        List<ParagraphResponse> filtered = paragraphs.stream()
                .filter(p -> (keyword == null || p.getJapaneseText().toLowerCase().contains(keyword.toLowerCase()) || (p.getVietnameseText() != null && p.getVietnameseText().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(p -> (level == null || p.getLevel() == level))
                .sorted((p1, p2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "japaneseText": cmp = p1.getJapaneseText().compareToIgnoreCase(p2.getJapaneseText()); break;
                        case "level": cmp = p1.getLevel().name().compareTo(p2.getLevel().name()); break;
                        case "id": default: cmp = p1.getId().compareTo(p2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(ParagraphResponse::fromParagraph)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<ParagraphResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<ParagraphResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    // ADMIN
    @Override
    public DataWithPageResponse<ParagraphAdminResponse> getAllParagraphsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<Paragraph> paragraphs = paragraphRepository.findAll();
        List<ParagraphAdminResponse> filtered = paragraphs.stream()
                .filter(p -> (keyword == null || p.getJapaneseText().toLowerCase().contains(keyword.toLowerCase()) || (p.getVietnameseText() != null && p.getVietnameseText().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(p -> (level == null || p.getLevel() == level))
                .sorted((p1, p2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "japaneseText": cmp = p1.getJapaneseText().compareToIgnoreCase(p2.getJapaneseText()); break;
                        case "level": cmp = p1.getLevel().name().compareTo(p2.getLevel().name()); break;
                        case "id": default: cmp = p1.getId().compareTo(p2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(ParagraphAdminResponse::fromParagraph)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<ParagraphAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<ParagraphAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public ParagraphAdminResponse getParagraphDetailForAdmin(Integer id) {
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.paragraph.not.found", id));
        return ParagraphAdminResponse.fromParagraph(paragraph);
    }

    @Override
    public ParagraphAdminResponse createParagraphForAdmin(ParagraphRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Paragraph paragraph = new Paragraph();
        paragraph.setJapaneseText(request.getJapaneseText());
        paragraph.setVietnameseText(request.getVietnameseText());
        paragraph.setLevel(request.getLevel());
        paragraph.setTopic(request.getTopic());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            paragraph.setAudioUrl(audioUrl);
        }
        paragraph.setCreateDate(java.time.LocalDateTime.now());
        paragraph.setCreateBy(adminUsername);
        paragraphRepository.save(paragraph);
        return ParagraphAdminResponse.fromParagraph(paragraph);
    }

    @Override
    public ParagraphAdminResponse updateParagraphForAdmin(Integer id, ParagraphRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.paragraph.not.found", id));
        paragraph.setJapaneseText(request.getJapaneseText());
        paragraph.setVietnameseText(request.getVietnameseText());
        paragraph.setLevel(request.getLevel());
        paragraph.setTopic(request.getTopic());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String oldAudioUrl = paragraph.getAudioUrl();
            if (oldAudioUrl != null && !oldAudioUrl.isEmpty() && oldAudioUrl.contains("cloudinary")) {
                cloudinaryService.deleteAudio(oldAudioUrl);
            }
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            paragraph.setAudioUrl(audioUrl);
        }
        paragraph.setUpdateDate(java.time.LocalDateTime.now());
        paragraph.setUpdateBy(adminUsername);
        paragraphRepository.save(paragraph);
        return ParagraphAdminResponse.fromParagraph(paragraph);
    }

    @Override
    public void deleteParagraphForAdmin(Integer id) {
        Paragraph paragraph = paragraphRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.paragraph.not.found", id));
        paragraphRepository.delete(paragraph);
    }
}