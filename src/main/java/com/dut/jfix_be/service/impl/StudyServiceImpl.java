package com.dut.jfix_be.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.request.StudyLogRequest;
import com.dut.jfix_be.dto.response.ReviewCardResponse;
import com.dut.jfix_be.dto.response.ReviewDeckResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.CorrectionHistory;
import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.entity.StudyLog;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.entity.UserDailyCardStat;
import com.dut.jfix_be.entity.UserErrorAnalytics;
import com.dut.jfix_be.entity.UserMistake;
import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.CorrectionHistoryRepository;
import com.dut.jfix_be.repository.FreeTalkTopicRepository;
import com.dut.jfix_be.repository.GrammarRepository;
import com.dut.jfix_be.repository.ParagraphRepository;
import com.dut.jfix_be.repository.SentenceRepository;
import com.dut.jfix_be.repository.SpeakingQuestionRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserDailyCardStatRepository;
import com.dut.jfix_be.repository.UserErrorAnalyticsRepository;
import com.dut.jfix_be.repository.UserMistakeRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.repository.VocabularyRepository;
import com.dut.jfix_be.service.StudyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyLogRepository studyLogRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final VocabularyRepository vocabularyRepository;
    private final GrammarRepository grammarRepository;
    private final ParagraphRepository paragraphRepository;
    private final SentenceRepository sentenceRepository;
    private final SpeakingQuestionRepository speakingQuestionRepository;
    private final FreeTalkTopicRepository freeTalkTopicRepository;
    private final MessageSource messageSource;
    private final UserMistakeRepository userMistakeRepository;
    private final UserErrorAnalyticsRepository userErrorAnalyticsRepository;
    private final CorrectionHistoryRepository correctionHistoryRepository;
    private final UserDailyCardStatRepository userDailyCardStatRepository;

    private String getGroupKey(CardType type, Skill skill) {
        return skill != null ? type + ":" + skill : type.toString();
    }

    private String getDeckName(CardType type, Skill skill) {
        StringBuilder name = new StringBuilder();
        
        switch (type) {
            case VOCABULARY:
                name.append("Từ vựng");
                break;
            case GRAMMAR:
                name.append("Ngữ pháp");
                break;
            case PARAGRAPH:
                name.append("Đoạn văn");
                break;
            case SENTENCE:
                name.append("Câu");
                break;
            case SPEAKING_QUESTION:
                name.append("Luyện nói");
                break;
            case FREE_TALK_TOPIC:
                name.append("Hội thoại tự do");
                break;
        }

        if (skill != null) {
            name.append(" - ");
            switch (skill) {
                case SPEAKING:
                    name.append("Luyện nói");
                    break;
                case SENTENCE_ARRANGEMENT:
                    name.append("Sắp xếp câu");
                    break;
                case FILL_IN_THE_BLANKS:
                    name.append("Điền vào chỗ trống");
                    break;
                case REPEAT:
                    name.append("Lặp lại");
                    break;
            }
        }

        return name.toString();
    }

    @Override
    public void updateStudyLog(StudyLogRequest request) {
        // Lấy study log và card liên quan
        StudyLog studyLog = studyLogRepository.findById(request.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.studylog.not.found", new Object[]{request.getId()}, LocaleContextHolder.getLocale())
            ));
        Card card = cardRepository.findById(studyLog.getCardId())
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.card.not.found", new Object[]{studyLog.getCardId()}, LocaleContextHolder.getLocale())
            ));

        // Lưu lại performance (độ khó) cho lần review này
        int performance = request.getPerformance() != null ? request.getPerformance() : 0;

        // Lấy các giá trị hiện tại
        int repetition = studyLog.getRepetition() != null ? studyLog.getRepetition() : 0;
        float interval = studyLog.getIntervals() != null ? studyLog.getIntervals() : 0f;
        float ef = studyLog.getEasinessFactor() != null ? studyLog.getEasinessFactor() : 2.5f;

        LocalDateTime nextReview;
        // Áp dụng thuật toán lặp lại ngắt quãng hợp lý hơn
        if (repetition == 0) {
            // Thẻ mới hoặc vừa trả lời sai
            if (performance == 0) {
                repetition = 0;
                interval = 0.0007f; // 1 phút
                nextReview = LocalDateTime.now().plusMinutes(1);
            } else if (performance == 1) {
                repetition = 0;
                interval = 0.007f; // 10 phút
                nextReview = LocalDateTime.now().plusMinutes(10);
            } else if (performance == 2) {
                repetition = 1;
                interval = 1f; // 1 ngày
                nextReview = LocalDateTime.now().plusDays(1);
            } else {
                repetition = 1;
                interval = 3f; // 3 ngày
                nextReview = LocalDateTime.now().plusDays(3);
            }
        } else {
            // Thẻ đã có lịch sử
            if (performance == 0) {
                repetition = 0;
                interval = 0.0007f;
                nextReview = LocalDateTime.now().plusMinutes(1);
            } else if (performance == 1) {
                // Không tăng repetition, lặp lại sau 10 phút
                interval = 0.007f;
                nextReview = LocalDateTime.now().plusMinutes(10);
            } else if (performance == 2) {
                repetition++;
                interval = Math.max(1f, interval * 1.7f); // tăng nhẹ
                nextReview = LocalDateTime.now().plusDays((long) interval);
            } else {
                repetition++;
                interval = Math.max(3f, interval * 2.5f); // tăng mạnh hơn
                if (interval > 180) interval = 180f;
                nextReview = LocalDateTime.now().plusDays((long) interval);
            }
        }
        // Cập nhật hệ số dễ nhớ (ef) như SM-2
        ef = ef + (0.1f - (3 - performance) * (0.08f + (3 - performance) * 0.02f));
        if (ef < 1.3f) ef = 1.3f;

        // Cập nhật lại study log
        studyLog.setRepetition(repetition);
        studyLog.setIntervals(interval);
        studyLog.setEasinessFactor(ef);
        studyLog.setLastReviewDate(studyLog.getReviewDate());
        studyLog.setReviewDate(nextReview);
        studyLog.setUpdateDate(java.time.LocalDateTime.now());
        studyLog.setUpdateBy(getCurrentUsername());
        studyLogRepository.save(studyLog);

        // Cập nhật thống kê số thẻ đã review mỗi ngày
        LocalDate statDate = LocalDate.now();
        Integer userId = studyLog.getUserId();

        // Lấy thống kê hiện tại
        UserDailyCardStat stat = userDailyCardStatRepository
            .findByUserIdAndStatDate(userId, statDate)
            .orElse(UserDailyCardStat.builder()
                .userId(userId)
                .statDate(statDate)
                .cardCount(0)
                .build());

        // Mỗi lần review đều tăng lên 1
        stat.setCardCount(stat.getCardCount() + 1);
        userDailyCardStatRepository.save(stat);

        // Nếu là thẻ tự luận thì lưu lại lỗi, phân tích lỗi, lịch sử sửa lỗi
        if (card.getType() == CardType.SENTENCE || card.getType() == CardType.PARAGRAPH || card.getType() == CardType.SPEAKING_QUESTION) {
            // Nếu có userInput hoặc feedback thì lưu lại
            if (request.getUserInput() != null || request.getFeedbackProvided() != null) {
                // --- XỬ LÝ USER_MISTAKE ---
                // Kiểm tra đã có lỗi trước đó chưa (theo userId + cardId)
                UserMistake mistake = userMistakeRepository.findByUserIdAndCardId(studyLog.getUserId(), card.getId())
                        .orElse(null);
                boolean isFirstMistake = false;
                if (mistake == null) {
                    // Lần đầu mắc lỗi, tạo mới bản ghi
                    mistake = UserMistake.builder()
                            .userId(studyLog.getUserId())
                            .cardId(card.getId())
                            .userInput(request.getUserInput())
                            .correctAnswer(request.getCorrectAnswer())
                            .feedbackProvided(request.getFeedbackProvided())
                            .wasCorrected(false)
                            .identifiedAt(java.time.LocalDateTime.now())
                            .createBy(getCurrentUsername())
                            .build();
                    userMistakeRepository.save(mistake);
                    isFirstMistake = true;
                } else {
                    // Đã có lỗi trước đó, chỉ update feedback nếu có
                    mistake.setFeedbackProvided(request.getFeedbackProvided());
                    mistake.setUpdateDate(java.time.LocalDateTime.now());
                    mistake.setUpdateBy(getCurrentUsername());
                    mistake.setWasCorrected(performance >= 2);
                    userMistakeRepository.save(mistake);
                }

                // --- XỬ LÝ USER_ERROR_ANALYTICS ---
                // Lấy thống kê lỗi cho user + card
                UserErrorAnalytics analytics = userErrorAnalyticsRepository.findByUserIdAndCardId(studyLog.getUserId(), card.getId())
                        .orElse(null);
                int totalAttempts = correctionHistoryRepository.countByUserMistakeId(mistake.getId()) + 1; // +1 cho lần hiện tại
                // Tổng số lần trả lời sai
                int totalWrong = 0;
                // Tổng số lần trả lời đúng khi gặp lại (không tính lần đầu)
                int totalRightOnReview = 0;
                // Đếm lại các bản ghi correction_history cho user_mistake này
                List<CorrectionHistory> histories = new ArrayList<>();
                histories = correctionHistoryRepository.findAllByUserMistakeId(mistake.getId());
                for (int i = 0; i < histories.size(); i++) {
                    CorrectionHistory h = histories.get(i);
                    if (h.getIsCorrect() != null && h.getIsCorrect()) {
                        if (i > 0) totalRightOnReview++; // chỉ tính đúng khi gặp lại
                    } else {
                        totalWrong++;
                    }
                }
                // Nếu lần hiện tại trả lời đúng và không phải lần đầu, tăng totalRightOnReview
                if (!isFirstMistake && performance >= 2) {
                    totalRightOnReview++;
                }
                // Nếu lần hiện tại trả lời sai, tăng totalWrong
                if (performance < 2) {
                    totalWrong++;
                }
                // Tính lại frequency và improvement_rate đúng nghiệp vụ
                float frequency = totalAttempts > 0 ? ((float) totalWrong) / totalAttempts : 0f;
                float improvementRate = (totalAttempts - 1) > 0 ? ((float) totalRightOnReview) / (totalAttempts - 1) : 0f;
                if (analytics == null) {
                    analytics = UserErrorAnalytics.builder()
                            .userId(studyLog.getUserId())
                            .cardId(card.getId())
                            .frequency(frequency)
                            .improvementRate(improvementRate)
                            .lastOccurrence(java.time.LocalDateTime.now())
                            .createBy(getCurrentUsername())
                            .build();
                } else {
                    analytics.setFrequency(frequency);
                    analytics.setImprovementRate(improvementRate);
                    analytics.setLastOccurrence(java.time.LocalDateTime.now());
                    analytics.setUpdateDate(java.time.LocalDateTime.now());
                    analytics.setUpdateBy(getCurrentUsername());
                }
                userErrorAnalyticsRepository.save(analytics);

                // --- XỬ LÝ CORRECTION_HISTORY ---
                // Mỗi lần nhập câu trả lời đều tạo mới bản ghi correction_history
                int attemptNumber = correctionHistoryRepository.countByUserMistakeId(mistake.getId()) + 1;
                CorrectionHistory correction = CorrectionHistory.builder()
                        .userMistakeId(mistake.getId())
                        .correctionAttempt(request.getUserInput())
                        .isCorrect(performance >= 2) // Nếu performance >= 2 coi như đã sửa đúng
                        .errorCorrectionTime(java.time.LocalDateTime.now())
                        .attemptNumber(attemptNumber)
                        .createBy(getCurrentUsername())
                        .build();
                correctionHistoryRepository.save(correction);
            }
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }

    private List<ReviewDeckResponse> getCardsByType(LocalDate date, CardType type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        // Lấy hết ngày được truyền vào (23:59:59)
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        List<StudyLog> studyLogs = studyLogRepository.findByUserIdAndReviewDateUpTo(
            user.getId(),
            endOfDay
        );

        // Group cards by type and skill
        Map<String, List<ReviewCardResponse>> groupedCards = new HashMap<>();

        for (StudyLog log : studyLogs) {
            Card card = cardRepository.findById(log.getCardId()).orElse(null);
            if (card == null || card.getType() != type) continue;
            ReviewCardResponse.ReviewCardResponseBuilder responseBuilder = ReviewCardResponse.builder()
                .id(log.getId())
                .cardId(card.getId())
                .type(card.getType())
                .skill(card.getSkill())
                .reviewDate(log.getReviewDate())
                .repetition(log.getRepetition())
                .intervals(log.getIntervals())
                .easinessFactor(log.getEasinessFactor());

            // Populate content based on card type
            switch (card.getType()) {
                case VOCABULARY:
                    Vocabulary vocab = vocabularyRepository.findById(card.getItemId()).orElse(null);
                    if (vocab != null) {
                        responseBuilder
                            .word(vocab.getWord())
                            .meaning(vocab.getMeaning())
                            .reading(vocab.getReading())
                            .example(vocab.getExampleWithoutReading())
                            .exampleMeaning(vocab.getExampleMeaning())
                            .level(vocab.getLevel())
                            .audioUrl(vocab.getAudio());
                    }
                    break;
                case GRAMMAR:
                    Grammar grammar = grammarRepository.findById(card.getItemId()).orElse(null);
                    if (grammar != null) {
                        responseBuilder
                            .structure(grammar.getStructure())
                            .usage(grammar.getUsage())
                            .meaning(grammar.getMeaning())
                            .example(grammar.getExample())
                            .exampleMeaning(grammar.getExampleMeaning())
                            .romaji(grammar.getRomaji())
                            .level(grammar.getLevel());
                    }
                    break;
                case PARAGRAPH:
                    Paragraph paragraph = paragraphRepository.findById(card.getItemId()).orElse(null);
                    if (paragraph != null) {
                        responseBuilder
                            .japaneseText(paragraph.getJapaneseText())
                            .vietnameseText(paragraph.getVietnameseText())
                            .topic(paragraph.getTopic())
                            .level(paragraph.getLevel());
                    }
                    break;
                case SENTENCE:
                    Sentence sentence = sentenceRepository.findById(card.getItemId()).orElse(null);
                    if (sentence != null) {
                        responseBuilder
                            .japaneseText(sentence.getJapaneseText())
                            .vietnameseText(sentence.getVietnameseText())
                            .level(sentence.getLevel());
                    }
                    break;
                case SPEAKING_QUESTION:
                    SpeakingQuestion question = speakingQuestionRepository.findById(card.getItemId()).orElse(null);
                    if (question != null) {
                        responseBuilder
                            .japaneseText(question.getJapaneseText())
                            .vietnameseText(question.getVietnameseText())
                            .sampleAnswerJapanese(question.getSampleAnswerJapanese())
                            .sampleAnswerVietnamese(question.getSampleAnswerVietnamese())
                            .level(question.getLevel());
                    }
                    break;
                case FREE_TALK_TOPIC:
                    FreeTalkTopic topic = freeTalkTopicRepository.findById(card.getItemId()).orElse(null);
                    if (topic != null) {
                        responseBuilder
                            .japaneseText(topic.getJapaneseText())
                            .vietnameseText(topic.getVietnameseText())
                            .guidelines(topic.getSampleAnswerVietnamese())
                            .level(topic.getLevel());
                    }
                    break;
            }

            UserMistake mistake = userMistakeRepository.findByUserIdAndCardId(log.getUserId(), card.getId()).orElse(null);
            if (mistake != null) {
                // Lấy thống kê analytics
                UserErrorAnalytics analytics = userErrorAnalyticsRepository.findByUserIdAndCardId(log.getUserId(), card.getId()).orElse(null);
                // Lấy danh sách correction histories
                List<CorrectionHistory> histories = correctionHistoryRepository.findAllByUserMistakeId(mistake.getId());
                List<com.dut.jfix_be.dto.response.UserMistakeHistoryDTO.CorrectionHistoryDTO> historyDTOs = histories.stream().map(h ->
                    com.dut.jfix_be.dto.response.UserMistakeHistoryDTO.CorrectionHistoryDTO.builder()
                        .correctionAttempt(h.getCorrectionAttempt())
                        .isCorrect(h.getIsCorrect())
                        .attemptNumber(h.getAttemptNumber())
                        .errorCorrectionTime(h.getErrorCorrectionTime())
                        .build()
                ).collect(Collectors.toList());
                // Build DTO
                responseBuilder.mistakeHistory(
                    com.dut.jfix_be.dto.response.UserMistakeHistoryDTO.builder()
                        .userInput(mistake.getUserInput())
                        .correctAnswer(mistake.getCorrectAnswer())
                        .identifiedAt(mistake.getIdentifiedAt())
                        .feedbackProvided(mistake.getFeedbackProvided())
                        .wasCorrected(mistake.getWasCorrected())
                        .frequency(analytics != null ? analytics.getFrequency() : null)
                        .improvementRate(analytics != null ? analytics.getImprovementRate() : null)
                        .lastOccurrence(analytics != null ? analytics.getLastOccurrence() : null)
                        .correctionHistories(historyDTOs)
                        .build()
                );
            }

            ReviewCardResponse cardResponse = responseBuilder.build();
            String key = getGroupKey(cardResponse.getType(), cardResponse.getSkill());
            groupedCards.computeIfAbsent(key, k -> new ArrayList<>()).add(cardResponse);
        }

        // Convert grouped cards to ReviewDeckResponse list, sort theo reviewDate
        return groupedCards.entrySet().stream()
            .map(entry -> {
                String[] parts = entry.getKey().split(":");
                CardType type1 = CardType.valueOf(parts[0]);
                Skill skill = parts.length > 1 ? Skill.valueOf(parts[1]) : null;
                List<ReviewCardResponse> sortedCards = entry.getValue();
                sortedCards.sort((a, b) -> a.getReviewDate().compareTo(b.getReviewDate()));
                return ReviewDeckResponse.builder()
                    .type(type1)
                    .skill(skill)
                    .deckName(getDeckName(type1, skill))
                    .cards(sortedCards)
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDeckResponse> getVocabularyCards(LocalDate date) {
        return getCardsByType(date, CardType.VOCABULARY);
    }

    @Override
    public List<ReviewDeckResponse> getGrammarCards(LocalDate date) {
        return getCardsByType(date, CardType.GRAMMAR);
    }

    @Override
    public List<ReviewDeckResponse> getParagraphCards(LocalDate date) {
        return getCardsByType(date, CardType.PARAGRAPH);
    }

    @Override
    public List<ReviewDeckResponse> getSentenceCards(LocalDate date) {
        return getCardsByType(date, CardType.SENTENCE);
    }

    @Override
    public List<ReviewDeckResponse> getSpeakingQuestionCards(LocalDate date) {
        return getCardsByType(date, CardType.SPEAKING_QUESTION);
    }

    @Override
    public List<ReviewDeckResponse> getFreeTalkTopicCards(LocalDate date) {
        return getCardsByType(date, CardType.FREE_TALK_TOPIC);
    }

    @Override
    public void updateVocabularyStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    @Override
    public void updateGrammarStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    @Override
    public void updateParagraphStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    @Override
    public void updateSentenceStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    @Override
    public void updateSpeakingQuestionStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    @Override
    public void updateFreeTalkTopicStudyLog(StudyLogRequest request) {
        updateStudyLog(request);
    }

    private List<ReviewDeckResponse> updateAndReturnSortedDecks(StudyLogRequest request, CardType type) {
        updateStudyLog(request);
        LocalDate today = LocalDate.now();
        List<ReviewDeckResponse> cards;
        switch (type) {
            case VOCABULARY:
                cards = getVocabularyCards(today); break;
            case GRAMMAR:
                cards = getGrammarCards(today); break;
            case PARAGRAPH:
                cards = getParagraphCards(today); break;
            case SENTENCE:
                cards = getSentenceCards(today); break;
            case SPEAKING_QUESTION:
                cards = getSpeakingQuestionCards(today); break;
            case FREE_TALK_TOPIC:
                cards = getFreeTalkTopicCards(today); break;
            default:
                cards = new ArrayList<>();
        }
        cards.forEach(deck -> deck.getCards().sort((a, b) -> a.getReviewDate().compareTo(b.getReviewDate())));
        return cards;
    }

    public List<ReviewDeckResponse> updateAndReturnParagraphDecks(StudyLogRequest request) {
        return updateAndReturnSortedDecks(request, CardType.PARAGRAPH);
    }
    public List<ReviewDeckResponse> updateAndReturnSentenceDecks(StudyLogRequest request) {
        return updateAndReturnSortedDecks(request, CardType.SENTENCE);
    }
    public List<ReviewDeckResponse> updateAndReturnSpeakingQuestionDecks(StudyLogRequest request) {
        return updateAndReturnSortedDecks(request, CardType.SPEAKING_QUESTION);
    }
    public List<ReviewDeckResponse> updateAndReturnFreeTalkTopicDecks(StudyLogRequest request) {
        return updateAndReturnSortedDecks(request, CardType.FREE_TALK_TOPIC);
    }
} 