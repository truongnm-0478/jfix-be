package com.dut.jfix_be.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.response.CardDifficultyResponse;
import com.dut.jfix_be.dto.response.ErrorImprovementResponse;
import com.dut.jfix_be.dto.response.ErrorRateResponse;
import com.dut.jfix_be.dto.response.StudyHeatmapResponse;
import com.dut.jfix_be.dto.response.TotalLearnedCardsResponse;
import com.dut.jfix_be.dto.response.UserAchievementResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.StudyLog;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.entity.UserAchievement;
import com.dut.jfix_be.entity.UserDailyCardStat;
import com.dut.jfix_be.entity.UserMistake;
import com.dut.jfix_be.enums.AchievementType;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserAchievementRepository;
import com.dut.jfix_be.repository.UserDailyCardStatRepository;
import com.dut.jfix_be.repository.UserMistakeRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.UserAchievementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAchievementServiceImpl implements UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final StudyLogRepository studyLogRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final CardRepository cardRepository;
    private final UserMistakeRepository userMistakeRepository;
    private final UserDailyCardStatRepository userDailyCardStatRepository;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException(messageSource.getMessage("error.user.not.authenticated", null, LocaleContextHolder.getLocale()));
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())));
    }

    @Override
    public List<UserAchievementResponse> getCurrentUserAchievements() {
        Integer userId = getCurrentUserId();
        return getUserAchievements(userId);
    }

    @Override
    public void calculateAndSaveCurrentUserAchievements() {
        Integer userId = getCurrentUserId();
        calculateAndSaveAchievements(userId);
    }

    @Override
    public List<UserAchievementResponse> calculateAndReturnCurrentUserAchievements() {
        Integer userId = getCurrentUserId();
        return calculateAndReturnAchievements(userId);
    }

    @Override
    public List<UserAchievementResponse> getUserAchievements(Integer userId) {
        List<UserAchievement> achievements = userAchievementRepository.findByUserId(userId);
        return achievements.stream()
            .map(a -> UserAchievementResponse.builder()
                .achievementType(a.getAchievementType())
                .achievementDate(a.getAchievementDate())
                .achievementValue(a.getAchievementValue())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public void calculateAndSaveAchievements(Integer userId) {
        // 1. Số thẻ đã hoàn thành: đếm số study log có repetition > 0 (tức là đã review ít nhất 1 lần)
        List<StudyLog> logs = studyLogRepository.findByUserId(userId);
        int completedCards = (int) logs.stream()
            .filter(log -> log.getRepetition() != null && log.getRepetition() > 0)
            .count();
        saveOrUpdateAchievement(userId, AchievementType.LESSON_COMPLETED, completedCards);

        int streakDays = calculateStreakDays(logs);
        saveOrUpdateAchievement(userId, AchievementType.STREAK_DAYS, streakDays);
    }

    private int calculateStreakDays(List<StudyLog> logs) {
        List<LocalDate> studyDates = logs.stream()
            .map(StudyLog::getUpdateDate)
            .filter(java.util.Objects::nonNull)
            .map(java.time.LocalDateTime::toLocalDate)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        if (studyDates.isEmpty()) return 0;
    
        int maxStreak = 1;
        int currentStreak = 1;
        for (int i = 1; i < studyDates.size(); i++) {
            if (studyDates.get(i).equals(studyDates.get(i - 1).plusDays(1))) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }
            if (currentStreak > maxStreak) maxStreak = currentStreak;
        }
        return maxStreak;
    }

    private void saveOrUpdateAchievement(Integer userId, AchievementType type, int value) {
        UserAchievement achievement = userAchievementRepository.findByUserIdAndAchievementType(userId, type)
            .orElse(UserAchievement.builder()
                .userId(userId)
                .achievementType(type)
                .createBy("SYSTEM")
                .build());

        achievement.setAchievementDate(LocalDateTime.now());
        achievement.setAchievementValue(value);
        achievement.setCreateDate(LocalDateTime.now());

        userAchievementRepository.save(achievement);
    }

    @Override
    public List<UserAchievementResponse> calculateAndReturnAchievements(Integer userId) {
        calculateAndSaveAchievements(userId);
        return getUserAchievements(userId);
    }

    @Override
    public List<StudyHeatmapResponse> getStudyHeatmap() {
        Integer userId = getCurrentUserId();
        List<UserDailyCardStat> stats = userDailyCardStatRepository.findAll().stream()
            .filter(stat -> stat.getUserId().equals(userId))
            .collect(Collectors.toList());
        return stats.stream()
            .map(stat -> StudyHeatmapResponse.builder().date(stat.getStatDate()).count(stat.getCardCount()).build())
            .sorted(Comparator.comparing(StudyHeatmapResponse::getDate))
            .collect(Collectors.toList());
    }

    @Override
    public List<StudyHeatmapResponse> getCardsOverTime() {
        return getStudyHeatmap(); // Có thể mở rộng nếu cần chi tiết hơn
    }


    @Override
    public ErrorRateResponse getErrorRate() {
        Integer userId = getCurrentUserId();
        List<StudyLog> studyLogs = studyLogRepository.findByUserId(userId);
        List<UserMistake> mistakes = userMistakeRepository.findAll()
            .stream().filter(m -> m.getUserId().equals(userId)).collect(Collectors.toList());

        // Tổng số thẻ đã học: chỉ lấy các cardId duy nhất có updateDate khác null
        long totalCards = studyLogs.stream()
            .filter(log -> log.getUpdateDate() != null)
            .map(StudyLog::getCardId)
            .distinct()
            .count();
        // Số thẻ mắc lỗi (cardId duy nhất trong UserMistake)
        long errorCards = mistakes.stream().map(UserMistake::getCardId).distinct().count();
        double errorRate = totalCards == 0 ? 0 : ((double) errorCards / totalCards);
        return ErrorRateResponse.builder()
            .correct((int)(totalCards - errorCards))
            .incorrect((int)errorCards)
            .errorRate(errorRate)
            .build();
    }

    @Override
    public List<ErrorImprovementResponse> getErrorImprovement() {
        Integer userId = getCurrentUserId();
        List<UserMistake> mistakes = userMistakeRepository.findAll()
            .stream().filter(m -> m.getUserId().equals(userId)).collect(Collectors.toList());
        Map<LocalDate, Long> map = mistakes.stream()
            .collect(Collectors.groupingBy(m -> m.getIdentifiedAt().toLocalDate(), Collectors.counting()));
        return map.entrySet().stream()
            .map(e -> ErrorImprovementResponse.builder().date(e.getKey()).errorCount(e.getValue().intValue()).build())
            .sorted(Comparator.comparing(ErrorImprovementResponse::getDate))
            .collect(Collectors.toList());
    }

    @Override
    public TotalLearnedCardsResponse getTotalLearnedCards() {
        Integer userId = getCurrentUserId();
        int total = (int) studyLogRepository.findByUserId(userId).stream()
            .filter(log -> log.getRepetition() != null && log.getRepetition() > 0)
            .count();
        return TotalLearnedCardsResponse.builder().total(total).build();
    }

    @Override
    public List<CardDifficultyResponse> getCardsByDifficulty() {
        Integer userId = getCurrentUserId();
        List<Card> allCards = cardRepository.findAll();
        List<StudyLog> logs = studyLogRepository.findByUserId(userId).stream()
            .filter(log -> log.getUpdateDate() != null)
            .collect(Collectors.toList());
        // Tính easiness_factor trung bình cho mỗi cardId đã học
        Map<Integer, Double> cardEasinessMap = logs.stream()
            .collect(Collectors.groupingBy(
                StudyLog::getCardId,
                Collectors.averagingDouble(StudyLog::getEasinessFactor)
            ));
        int easy = 0, medium = 0, hard = 0;
        for (double ef : cardEasinessMap.values()) {
            if (ef < 2.0) hard++;
            else if (ef < 2.3) medium++;
            else easy++;
        }
        // Số thẻ đã học
        Set<Integer> learnedCardIds = cardEasinessMap.keySet();
        // Số thẻ chưa học = tổng số cardId - số đã học
        int notLearned = (int) allCards.stream()
            .map(Card::getId)
            .filter(id -> !learnedCardIds.contains(id))
            .count();
        List<CardDifficultyResponse> result = List.of(
            CardDifficultyResponse.builder().difficulty("Khó").count(hard).build(),
            CardDifficultyResponse.builder().difficulty("Trung bình").count(medium).build(),
            CardDifficultyResponse.builder().difficulty("Dễ").count(easy).build(),
            CardDifficultyResponse.builder().difficulty("Chưa học").count(notLearned).build()
        );
        return result;
    }
} 