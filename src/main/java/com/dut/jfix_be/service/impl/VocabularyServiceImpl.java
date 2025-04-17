package com.dut.jfix_be.service.impl;

import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.VocabularyRepository;
import com.dut.jfix_be.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    @Override
    public VocabularyResponse findById(Integer id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.vocabulary.notfound"));
        return mapToResponse(vocabulary);
    }

    @Override
    public List<VocabularyResponse> findByLevel(JlptLevel level) {
        List<Vocabulary> vocabularies = vocabularyRepository.findByLevel(level);
        return vocabularies.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public VocabularyResponse createVocabulary(VocabularyResponse vocabularyResponse) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setWord(vocabularyResponse.getWord());
        vocabulary.setReading(vocabularyResponse.getReading());
        vocabulary.setMeaning(vocabularyResponse.getMeaning());
        vocabulary.setExampleWithReading(vocabularyResponse.getExampleWithReading());
        vocabulary.setExampleWithoutReading(vocabularyResponse.getExampleWithoutReading());
        vocabulary.setExampleMeaning(vocabularyResponse.getExampleMeaning());
        vocabulary.setAudio(vocabularyResponse.getAudio());
        vocabulary.setLevel(vocabularyResponse.getLevel() != null ? vocabularyResponse.getLevel() : JlptLevel.FREE);
        vocabulary.setChapter(vocabularyResponse.getChapter());
        vocabulary.setSection(vocabularyResponse.getSection());
        vocabulary.setCreateDate(LocalDateTime.now());
        vocabulary.setCreateBy("system");

        Vocabulary savedVocabulary = vocabularyRepository.save(vocabulary);
        return mapToResponse(savedVocabulary);
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