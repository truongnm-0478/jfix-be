package com.dut.jfix_be.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.VocabularyAdminRequest;
import com.dut.jfix_be.dto.response.VocabularyAdminResponse;
import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.VocabularyRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.VocabularyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final CloudinaryService cloudinaryService;
    private final CardRepository cardRepository;
    private final StudyLogRepository studyLogRepository;

    @Override
    public VocabularyResponse findById(Integer id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.vocabulary.not.found", id));
        return mapToResponse(vocabulary);
    }

    @Override
    public List<VocabularyResponse> findByLevel(JlptLevel level) {
        List<Vocabulary> vocabularies = vocabularyRepository.findByLevel(level);
        return vocabularies.stream()
                .map(VocabularyResponse::fromVocabulary)
                .collect(Collectors.toList());
    }

    @Override
    public DataWithPageResponse<VocabularyResponse> getAllVocabularies(String keyword, JlptLevel level, String chapter, String section, int page, int size, String sortBy, String sortDir) {
        List<VocabularyResponse> filtered = vocabularyRepository.findAll().stream()
                .map(VocabularyResponse::fromVocabulary)
                .filter(v -> keyword == null || v.getWord().toLowerCase().contains(keyword.toLowerCase()) || (v.getMeaning() != null && v.getMeaning().toLowerCase().contains(keyword.toLowerCase())))
                .filter(v -> level == null || v.getLevel() == level)
                .filter(v -> chapter == null || (v.getChapter() != null && v.getChapter().equalsIgnoreCase(chapter)))
                .filter(v -> section == null || (v.getSection() != null && v.getSection().equalsIgnoreCase(section)))
                .sorted((v1, v2) -> {
                    Comparator<VocabularyResponse> comparator;
                    switch (sortBy) {
                        case "word": comparator = Comparator.comparing(VocabularyResponse::getWord, String.CASE_INSENSITIVE_ORDER); break;
                        case "meaning": comparator = Comparator.comparing(VocabularyResponse::getMeaning, String.CASE_INSENSITIVE_ORDER); break;
                        case "level": comparator = Comparator.comparing(v -> v.getLevel().name()); break;
                        case "id": default: comparator = Comparator.comparing(VocabularyResponse::getId); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? comparator.reversed().compare(v1, v2) : comparator.compare(v1, v2);
                })
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<VocabularyResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<VocabularyResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public DataWithPageResponse<VocabularyAdminResponse> getAllVocabulariesForAdmin(String keyword, JlptLevel level, String chapter, String section, int page, int size, String sortBy, String sortDir) {
        List<VocabularyAdminResponse> filtered = vocabularyRepository.findAll().stream()
                .map(VocabularyAdminResponse::fromVocabulary)
                .filter(v -> keyword == null || v.getWord().toLowerCase().contains(keyword.toLowerCase()) || (v.getMeaning() != null && v.getMeaning().toLowerCase().contains(keyword.toLowerCase())))
                .filter(v -> level == null || v.getLevel() == level)
                .filter(v -> chapter == null || (v.getChapter() != null && v.getChapter().equalsIgnoreCase(chapter)))
                .filter(v -> section == null || (v.getSection() != null && v.getSection().equalsIgnoreCase(section)))
                .sorted((v1, v2) -> {
                    Comparator<VocabularyAdminResponse> comparator;
                    switch (sortBy) {
                        case "word": comparator = Comparator.comparing(VocabularyAdminResponse::getWord, String.CASE_INSENSITIVE_ORDER); break;
                        case "meaning": comparator = Comparator.comparing(VocabularyAdminResponse::getMeaning, String.CASE_INSENSITIVE_ORDER); break;
                        case "level": comparator = Comparator.comparing(v -> v.getLevel().name()); break;
                        case "id": default: comparator = Comparator.comparing(VocabularyAdminResponse::getId); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? comparator.reversed().compare(v1, v2) : comparator.compare(v1, v2);
                })
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<VocabularyAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<VocabularyAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public VocabularyAdminResponse getVocabularyDetailForAdmin(Integer id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.vocabulary.not.found", id));
        return VocabularyAdminResponse.fromVocabulary(vocabulary);
    }

    @Override
    public VocabularyAdminResponse createVocabularyForAdmin(VocabularyAdminRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setWord(request.getWord());
        vocabulary.setReading(request.getReading());
        vocabulary.setMeaning(request.getMeaning());
        vocabulary.setExampleWithReading(request.getExampleWithReading());
        vocabulary.setExampleWithoutReading(request.getExampleWithoutReading());
        vocabulary.setExampleMeaning(request.getExampleMeaning());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            vocabulary.setAudio(audioUrl);
        }
        vocabulary.setLevel(request.getLevel());
        vocabulary.setChapter(request.getChapter());
        vocabulary.setSection(request.getSection());
        vocabulary.setCreateDate(java.time.LocalDateTime.now());
        vocabulary.setCreateBy(adminUsername);
        vocabularyRepository.save(vocabulary);
        return VocabularyAdminResponse.fromVocabulary(vocabulary);
    }

    @Override
    public VocabularyAdminResponse updateVocabularyForAdmin(Integer id, VocabularyAdminRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.vocabulary.not.found", id));
        vocabulary.setWord(request.getWord());
        vocabulary.setReading(request.getReading());
        vocabulary.setMeaning(request.getMeaning());
        vocabulary.setExampleWithReading(request.getExampleWithReading());
        vocabulary.setExampleWithoutReading(request.getExampleWithoutReading());
        vocabulary.setExampleMeaning(request.getExampleMeaning());
        if (request.getAudio() != null && !request.getAudio().isEmpty()) {
            String oldAudioUrl = vocabulary.getAudio();
            if (oldAudioUrl != null && !oldAudioUrl.isEmpty() && oldAudioUrl.contains("cloudinary")) {
                cloudinaryService.deleteAudio(oldAudioUrl);
            }
            String audioUrl = cloudinaryService.uploadAudio(request.getAudio());
            vocabulary.setAudio(audioUrl);
        }
        vocabulary.setLevel(request.getLevel());
        vocabulary.setChapter(request.getChapter());
        vocabulary.setSection(request.getSection());
        vocabulary.setUpdateDate(java.time.LocalDateTime.now());
        vocabulary.setUpdateBy(adminUsername);
        vocabularyRepository.save(vocabulary);
        return VocabularyAdminResponse.fromVocabulary(vocabulary);
    }

    @Override
    public void deleteVocabularyForAdmin(Integer id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.vocabulary.not.found", id));
        String audioUrl = vocabulary.getAudio();
        if (audioUrl != null && !audioUrl.isEmpty() && audioUrl.contains("cloudinary")) {
            cloudinaryService.deleteAudio(audioUrl);
        }
        List<Card> cards = cardRepository.findByTypeAndItemId(CardType.VOCABULARY, id);
        for (Card card : cards) {
            studyLogRepository.deleteAllByCardId(card.getId());
            cardRepository.delete(card);
        }
        vocabularyRepository.delete(vocabulary);
    }

    private VocabularyResponse mapToResponse(Vocabulary vocabulary) {
        VocabularyResponse response = new VocabularyResponse();
        response.setId(vocabulary.getId());
        response.setWord(vocabulary.getWord());
        response.setReading(vocabulary.getReading());
        response.setMeaning(vocabulary.getMeaning());
        response.setExampleWithReading(vocabulary.getExampleWithReading());
        response.setExampleWithoutReading(vocabulary.getExampleWithoutReading());
        response.setExampleMeaning(vocabulary.getExampleMeaning());
        response.setAudio(vocabulary.getAudio());
        response.setLevel(vocabulary.getLevel());
        response.setChapter(vocabulary.getChapter());
        response.setSection(vocabulary.getSection());
        return response;
    }
}