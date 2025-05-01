package com.dut.jfix_be.service.impl;

import java.time.LocalDate;
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

import com.dut.jfix_be.dto.response.ReviewCardResponse;
import com.dut.jfix_be.dto.response.ReviewDeckResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.entity.StudyLog;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.FreeTalkTopicRepository;
import com.dut.jfix_be.repository.GrammarRepository;
import com.dut.jfix_be.repository.ParagraphRepository;
import com.dut.jfix_be.repository.SentenceRepository;
import com.dut.jfix_be.repository.SpeakingQuestionRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.repository.VocabularyRepository;
import com.dut.jfix_be.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

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

    @Override
    public List<ReviewDeckResponse> getReviewCards(LocalDate date, CardType filterType, Skill filterSkill) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        // Get study logs for the specified date
        List<StudyLog> studyLogs = studyLogRepository.findByUserIdAndReviewDate(
            user.getId(), 
            date.atStartOfDay(), 
            date.plusDays(1).atStartOfDay()
        );

        // Group cards by type and skill
        Map<String, List<ReviewCardResponse>> groupedCards = new HashMap<>();

        for (StudyLog log : studyLogs) {
            Card card = cardRepository.findById(log.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.card.not.found", new Object[]{log.getCardId()}, LocaleContextHolder.getLocale())
                ));

            // Skip if type filter is set and doesn't match
            if (filterType != null && card.getType() != filterType) {
                continue;
            }

            // Skip if skill filter is set and doesn't match
            // When filterSkill is null, only return cards with null skill
            if ((filterSkill == null && card.getSkill() != null) || 
                (filterSkill != null && card.getSkill() != filterSkill)) {
                continue;
            }

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
                    Vocabulary vocab = vocabularyRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.vocabulary.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .word(vocab.getWord())
                        .meaning(vocab.getMeaning())
                        .reading(vocab.getReading())
                        .example(vocab.getExampleWithoutReading())
                        .exampleMeaning(vocab.getExampleMeaning())
                        .level(vocab.getLevel())
                        .audioUrl(vocab.getAudio());
                    break;

                case GRAMMAR:
                    Grammar grammar = grammarRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.grammar.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .structure(grammar.getStructure())
                        .usage(grammar.getUsage())
                        .meaning(grammar.getMeaning())
                        .example(grammar.getExample())
                        .exampleMeaning(grammar.getExampleMeaning())
                        .romaji(grammar.getRomaji())
                        .level(grammar.getLevel());
                    break;

                case PARAGRAPH:
                    Paragraph paragraph = paragraphRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.paragraph.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .japaneseText(paragraph.getJapaneseText())
                        .vietnameseText(paragraph.getVietnameseText())
                        .topic(paragraph.getTopic())
                        .level(paragraph.getLevel());
                    break;

                case SENTENCE:
                    Sentence sentence = sentenceRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.sentence.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .japaneseText(sentence.getJapaneseText())
                        .vietnameseText(sentence.getVietnameseText())
                        .level(sentence.getLevel());
                    break;

                case SPEAKING_QUESTION:
                    SpeakingQuestion question = speakingQuestionRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.speaking.question.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .japaneseText(question.getJapaneseText())
                        .vietnameseText(question.getVietnameseText())
                        .sampleAnswerJapanese(question.getSampleAnswerJapanese())
                        .sampleAnswerVietnamese(question.getSampleAnswerVietnamese())
                        .level(question.getLevel());
                    break;

                case FREE_TALK_TOPIC:
                    FreeTalkTopic topic = freeTalkTopicRepository.findById(card.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.free.talk.topic.not.found", new Object[]{card.getItemId()}, LocaleContextHolder.getLocale())
                        ));
                    responseBuilder
                        .japaneseText(topic.getJapaneseText())
                        .vietnameseText(topic.getVietnameseText())
                        .guidelines(topic.getSampleAnswerVietnamese())
                        .level(topic.getLevel());
                    break;
            }

            ReviewCardResponse cardResponse = responseBuilder.build();
            String key = getGroupKey(cardResponse.getType(), cardResponse.getSkill());
            groupedCards.computeIfAbsent(key, k -> new ArrayList<>()).add(cardResponse);
        }

        // Convert grouped cards to ReviewDeckResponse list
        return groupedCards.entrySet().stream()
            .map(entry -> {
                String[] parts = entry.getKey().split(":");
                CardType type = CardType.valueOf(parts[0]);
                Skill skill = parts.length > 1 ? Skill.valueOf(parts[1]) : null;
                
                return ReviewDeckResponse.builder()
                    .type(type)
                    .skill(skill)
                    .deckName(getDeckName(type, skill))
                    .cards(entry.getValue())
                    .build();
            })
            .collect(Collectors.toList());
    }

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
} 