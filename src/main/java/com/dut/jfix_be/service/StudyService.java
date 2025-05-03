package com.dut.jfix_be.service;

import java.time.LocalDate;
import java.util.List;

import com.dut.jfix_be.dto.request.StudyLogRequest;
import com.dut.jfix_be.dto.response.ReviewDeckResponse;

public interface StudyService {
    void updateStudyLog(StudyLogRequest request);

    List<ReviewDeckResponse> getVocabularyCards(LocalDate date);
    List<ReviewDeckResponse> getGrammarCards(LocalDate date);
    List<ReviewDeckResponse> getParagraphCards(LocalDate date);
    List<ReviewDeckResponse> getSentenceCards(LocalDate date);
    List<ReviewDeckResponse> getSpeakingQuestionCards(LocalDate date);
    List<ReviewDeckResponse> getFreeTalkTopicCards(LocalDate date);

    void updateVocabularyStudyLog(StudyLogRequest request);
    void updateGrammarStudyLog(StudyLogRequest request);
    void updateParagraphStudyLog(StudyLogRequest request);
    void updateSentenceStudyLog(StudyLogRequest request);
    void updateSpeakingQuestionStudyLog(StudyLogRequest request);
    void updateFreeTalkTopicStudyLog(StudyLogRequest request);

    List<ReviewDeckResponse> updateAndReturnParagraphDecks(StudyLogRequest request);
    List<ReviewDeckResponse> updateAndReturnSentenceDecks(StudyLogRequest request);
    List<ReviewDeckResponse> updateAndReturnSpeakingQuestionDecks(StudyLogRequest request);
    List<ReviewDeckResponse> updateAndReturnFreeTalkTopicDecks(StudyLogRequest request);
} 