package com.dut.jfix_be.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dut.jfix_be.dto.request.CardRequest;
import com.dut.jfix_be.dto.request.DeckRequest;
import com.dut.jfix_be.dto.request.LearningGoalRequest;
import com.dut.jfix_be.dto.response.CardResponse;
import com.dut.jfix_be.dto.response.DeckResponse;
import com.dut.jfix_be.dto.response.LearningGoalResponse;
import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.entity.LearningGoal;
import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.entity.StudyLog;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.enums.Skill;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.FreeTalkTopicRepository;
import com.dut.jfix_be.repository.GrammarRepository;
import com.dut.jfix_be.repository.LearningGoalRepository;
import com.dut.jfix_be.repository.ParagraphRepository;
import com.dut.jfix_be.repository.SentenceRepository;
import com.dut.jfix_be.repository.SpeakingQuestionRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.repository.VocabularyRepository;
import com.dut.jfix_be.service.CardService;
import com.dut.jfix_be.service.DeckService;
import com.dut.jfix_be.service.LearningGoalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningGoalServiceImpl implements LearningGoalService {

    private final LearningGoalRepository learningGoalRepository;
    private final UserRepository userRepository;
    private final DeckService deckService;
    private final CardService cardService;
    private final VocabularyRepository vocabularyRepository;
    private final GrammarRepository grammarRepository;
    private final ParagraphRepository paragraphRepository;
    private final SpeakingQuestionRepository speakingQuestionRepository;
    private final FreeTalkTopicRepository freeTalkTopicRepository;
    private final MessageSource messageSource;
    private final SentenceRepository sentenceRepository;
    private final StudyLogRepository studyLogRepository;

    @Override
    @Transactional
    public LearningGoalResponse createLearningGoal(LearningGoalRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
        
        // Check if user already has an active learning goal with same level
        if (hasExistingGoalWithLevel(user.getId(), request.getTargetLevel())) {
            throw new RuntimeException(
                messageSource.getMessage("error.learning.goal.exists.same.level", null, LocaleContextHolder.getLocale())
            );
        }

        LearningGoal learningGoal = LearningGoal.builder()
                .userId(user.getId())
                .targetLevel(request.getTargetLevel())
                .description(request.getDescription())
                .dailyMinutes(request.getDailyMinutes())
                .dailyVocabTarget(request.getDailyVocabTarget())
                .targetDate(request.getTargetDate())
                .createDate(LocalDateTime.now())
                .createBy(username)
                .build();

        LearningGoal savedGoal = learningGoalRepository.save(learningGoal);
        LearningGoalResponse response = LearningGoalResponse.fromLearningGoal(savedGoal);
        
        // Setup initial learning decks based on the goal only if target level is not FREE
        if (request.getTargetLevel() != JlptLevel.FREE) {
            setupInitialLearningDecks(user.getId(), response);
        }
        
        return response;
    }

    @Override
    @Transactional
    public void setupInitialLearningDecks(Integer userId, LearningGoalResponse goal) {
        // Calculate days between now and target date
        long totalDays = ChronoUnit.DAYS.between(LocalDate.now(), goal.getTargetDate());

        // Create decks for each content type
        DeckRequest vocabDeck = createDeck("Tango", goal.getTargetLevel());
        DeckRequest grammarDeck = createDeck("Bunpou", goal.getTargetLevel());
        DeckRequest paragraphDeck = createDeck("Danraku", goal.getTargetLevel());
        DeckRequest sentenceDeck = createDeck("Bun", goal.getTargetLevel());
        DeckRequest freeTalkDeck = createDeck("Kaiwa", goal.getTargetLevel());
        DeckRequest speakingDeck = createDeck("Shitsumon", goal.getTargetLevel());

        // Create decks and get responses
        DeckResponse vocabDeckResponse = deckService.createDeck(vocabDeck);
        DeckResponse grammarDeckResponse = deckService.createDeck(grammarDeck);
        DeckResponse paragraphDeckResponse = deckService.createDeck(paragraphDeck);
        DeckResponse sentenceDeckResponse = deckService.createDeck(sentenceDeck);
        DeckResponse freeTalkDeckResponse = deckService.createDeck(freeTalkDeck);
        DeckResponse speakingDeckResponse = deckService.createDeck(speakingDeck);

        // Get all content for the target level
        List<Vocabulary> vocabularies = vocabularyRepository.findByLevel(goal.getTargetLevel());
        List<Grammar> grammars = grammarRepository.findByLevel(goal.getTargetLevel());
        List<Paragraph> paragraphs = paragraphRepository.findByLevel(goal.getTargetLevel());
        List<Sentence> sentences = sentenceRepository.findByLevel(goal.getTargetLevel());
        List<FreeTalkTopic> freeTalkTopics = freeTalkTopicRepository.findByLevel(goal.getTargetLevel());
        List<SpeakingQuestion> speakingQuestions = speakingQuestionRepository.findByLevel(goal.getTargetLevel());

        // Calculate items per day
        int vocabPerDay = calculateItemsPerDay(vocabularies.size(), totalDays);
        int grammarPerDay = calculateItemsPerDay(grammars.size(), totalDays);
        int paragraphPerDay = calculateItemsPerDay(paragraphs.size(), totalDays);
        int sentencePerDay = calculateItemsPerDay(sentences.size(), totalDays);
        int freeTalkPerDay = calculateItemsPerDay(freeTalkTopics.size(), totalDays);
        int speakingPerDay = calculateItemsPerDay(speakingQuestions.size(), totalDays);

        // Create cards and study logs for each day
        LocalDate currentDate = LocalDate.now();
        int vocabIndex = 0, grammarIndex = 0, paragraphIndex = 0;
        int sentenceIndex = 0, freeTalkIndex = 0, speakingIndex = 0;

        for (int day = 0; day < totalDays; day++) {
            LocalDate reviewDate = currentDate.plusDays(day);
            
            // Process vocabulary cards
            for (int i = 0; i < vocabPerDay && vocabIndex < vocabularies.size(); i++, vocabIndex++) {
                Vocabulary vocab = vocabularies.get(vocabIndex);
                createCardWithStudyLog(userId, vocabDeckResponse.getId(), CardType.VOCABULARY, vocab.getId(), null, reviewDate);
            }

            // Process grammar cards
            for (int i = 0; i < grammarPerDay && grammarIndex < grammars.size(); i++, grammarIndex++) {
                Grammar grammar = grammars.get(grammarIndex);
                createCardWithStudyLog(userId, grammarDeckResponse.getId(), CardType.GRAMMAR, grammar.getId(), null, reviewDate);
            }

            // Process paragraph cards
            for (int i = 0; i < paragraphPerDay && paragraphIndex < paragraphs.size(); i++, paragraphIndex++) {
                Paragraph paragraph = paragraphs.get(paragraphIndex);
                createCardWithStudyLog(userId, paragraphDeckResponse.getId(), CardType.PARAGRAPH, paragraph.getId(), Skill.FILL_IN_THE_BLANKS, reviewDate);
            }

            // Process sentence cards
            for (int i = 0; i < sentencePerDay && sentenceIndex < sentences.size(); i++, sentenceIndex++) {
                Sentence sentence = sentences.get(sentenceIndex);
                createCardWithStudyLog(userId, sentenceDeckResponse.getId(), CardType.SENTENCE, sentence.getId(), Skill.REPEAT, reviewDate);
            }

            // Process speaking question cards
            for (int i = 0; i < speakingPerDay && speakingIndex < speakingQuestions.size(); i++, speakingIndex++) {
                SpeakingQuestion question = speakingQuestions.get(speakingIndex);
                createCardWithStudyLog(userId, speakingDeckResponse.getId(), CardType.SPEAKING_QUESTION, question.getId(), Skill.SPEAKING, reviewDate);
            }

            // Process free talk topic cards
            for (int i = 0; i < freeTalkPerDay && freeTalkIndex < freeTalkTopics.size(); i++, freeTalkIndex++) {
                FreeTalkTopic topic = freeTalkTopics.get(freeTalkIndex);
                createCardWithStudyLog(userId, freeTalkDeckResponse.getId(), CardType.FREE_TALK_TOPIC, topic.getId(), Skill.SPEAKING, reviewDate);
            }
        }

        // Handle remaining items if any
        processRemainingItems(userId, vocabDeckResponse.getId(), vocabularies, vocabIndex, goal.getTargetDate());
        processRemainingItems(userId, grammarDeckResponse.getId(), grammars, grammarIndex, goal.getTargetDate());
        processRemainingItems(userId, paragraphDeckResponse.getId(), paragraphs, paragraphIndex, goal.getTargetDate());
        processRemainingItems(userId, sentenceDeckResponse.getId(), sentences, sentenceIndex, goal.getTargetDate());
        processRemainingItems(userId, speakingDeckResponse.getId(), speakingQuestions, speakingIndex, goal.getTargetDate());
        processRemainingItems(userId, freeTalkDeckResponse.getId(), freeTalkTopics, freeTalkIndex, goal.getTargetDate());
    }

    private int calculateItemsPerDay(int totalItems, long totalDays) {
        return (int) Math.ceil((double) totalItems / totalDays);
    }

    private void createCardWithStudyLog(Integer userId, Integer deckId, CardType type, Integer itemId, Skill skill, LocalDate reviewDate) {
        // Create card
        CardRequest cardRequest = CardRequest.builder()
                .deckId(deckId)
                .type(type)
                .itemId(itemId)
                .skill(skill)
                .build();
        
        CardResponse cardResponse = cardService.createCard(cardRequest);

        // Create study log
        StudyLog studyLog = StudyLog.builder()
                .userId(userId)
                .cardId(cardResponse.getId())
                .reviewDate(reviewDate.atStartOfDay())
                .repetition(0)
                .intervals(0f)
                .easinessFactor(2.5f)
                .createDate(LocalDateTime.now())
                .createBy("SYSTEM")
                .build();

        studyLogRepository.save(studyLog);
    }

    private <T> void processRemainingItems(Integer userId, Integer deckId, List<T> items, int startIndex, LocalDate targetDate) {
        for (int i = startIndex; i < items.size(); i++) {
            T item = items.get(i);
            if (item instanceof Vocabulary) {
                createCardWithStudyLog(userId, deckId, CardType.VOCABULARY, ((Vocabulary) item).getId(), null, targetDate);
                createCardWithStudyLog(userId, deckId, CardType.VOCABULARY, ((Vocabulary) item).getId(), Skill.SENTENCE_ARRANGEMENT, targetDate);
                createCardWithStudyLog(userId, deckId, CardType.VOCABULARY, ((Vocabulary) item).getId(), Skill.REPEAT, targetDate);
            } else if (item instanceof Grammar) {
                createCardWithStudyLog(userId, deckId, CardType.GRAMMAR, ((Grammar) item).getId(), null, targetDate);
                createCardWithStudyLog(userId, deckId, CardType.GRAMMAR, ((Grammar) item).getId(), Skill.SENTENCE_ARRANGEMENT, targetDate);
                createCardWithStudyLog(userId, deckId, CardType.GRAMMAR, ((Grammar) item).getId(), Skill.REPEAT, targetDate);
            } else if (item instanceof Paragraph) {
                createCardWithStudyLog(userId, deckId, CardType.PARAGRAPH, ((Paragraph) item).getId(), Skill.FILL_IN_THE_BLANKS, targetDate);
            } else if (item instanceof Sentence) {
                createCardWithStudyLog(userId, deckId, CardType.SENTENCE, ((Sentence) item).getId(), Skill.SENTENCE_ARRANGEMENT, targetDate);
                createCardWithStudyLog(userId, deckId, CardType.SENTENCE, ((Sentence) item).getId(), Skill.REPEAT, targetDate);
            } else if (item instanceof SpeakingQuestion) {
                createCardWithStudyLog(userId, deckId, CardType.SPEAKING_QUESTION, ((SpeakingQuestion) item).getId(), Skill.SPEAKING, targetDate);
            } else if (item instanceof FreeTalkTopic) {
                createCardWithStudyLog(userId, deckId, CardType.FREE_TALK_TOPIC, ((FreeTalkTopic) item).getId(), Skill.SPEAKING, targetDate);
            }
        }
    }

    private DeckRequest createDeck(String type, JlptLevel level) {
        return DeckRequest.builder()
                .name(level + " " + type)
                .description(level + " " + type.toLowerCase())
                .build();
    }

    @Override
    public LearningGoalResponse getLearningGoal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
        
        LearningGoal learningGoal = learningGoalRepository.findActiveGoalByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.learning.goal.not.found", null, LocaleContextHolder.getLocale())
                ));
                
        return LearningGoalResponse.fromLearningGoal(learningGoal);
    }

    @Override
    public boolean hasExistingGoal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
            
        return hasExistingGoalWithLevel(user.getId(), null);
    }

    private boolean hasExistingGoalWithLevel(Integer userId, JlptLevel level) {
        return learningGoalRepository.existsActiveGoalByUserIdAndLevel(userId, level);
    }
} 