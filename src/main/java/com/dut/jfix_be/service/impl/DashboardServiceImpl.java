package com.dut.jfix_be.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.StudyLog;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.entity.UserDailyCardStat;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserDailyCardStatRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final StudyLogRepository studyLogRepository;
    private final UserDailyCardStatRepository userDailyCardStatRepository;

    @Override
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> result = new HashMap<>();
        
        // Get total users count
        long totalUsers = userRepository.count();
        result.put("totalUsers", totalUsers);
        
        // Get lessons count (total of all card types)
        List<Card> allCards = cardRepository.findAll();
        long vocabularyCount = allCards.stream().filter(card -> card.getType() == CardType.VOCABULARY).count();
        long grammarCount = allCards.stream().filter(card -> card.getType() == CardType.GRAMMAR).count();
        long paragraphCount = allCards.stream().filter(card -> card.getType() == CardType.PARAGRAPH).count();
        long sentenceCount = allCards.stream().filter(card -> card.getType() == CardType.SENTENCE).count();
        long speakingQuestionCount = allCards.stream().filter(card -> card.getType() == CardType.SPEAKING_QUESTION).count();
        long freeTalkTopicCount = allCards.stream().filter(card -> card.getType() == CardType.FREE_TALK_TOPIC).count();
        
        long totalLessons = vocabularyCount + grammarCount + paragraphCount + 
                           sentenceCount + speakingQuestionCount + freeTalkTopicCount;
        
        result.put("totalLessons", totalLessons);
        result.put("vocabularyCount", vocabularyCount);
        result.put("grammarCount", grammarCount);
        result.put("paragraphCount", paragraphCount);
        result.put("sentenceCount", sentenceCount);
        result.put("speakingQuestionCount", speakingQuestionCount);
        result.put("freeTalkTopicCount", freeTalkTopicCount);
        
        // Get average card study stats per user for current month
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.of(now.getYear(), now.getMonth());
        LocalDate startOfCurrentMonth = currentMonth.atDay(1);
        LocalDate endOfCurrentMonth = currentMonth.atEndOfMonth();
        
        // Calculate average cards studied per user in current month
        List<UserDailyCardStat> currentMonthStats = userDailyCardStatRepository.findAll().stream()
                .filter(stat -> !stat.getStatDate().isBefore(startOfCurrentMonth) && !stat.getStatDate().isAfter(endOfCurrentMonth))
                .collect(Collectors.toList());
        
        Map<Integer, Integer> userCardCountMap = new HashMap<>();
        for (UserDailyCardStat stat : currentMonthStats) {
            userCardCountMap.put(stat.getUserId(), 
                    userCardCountMap.getOrDefault(stat.getUserId(), 0) + stat.getCardCount());
        }
        
        double averageCardsPerUser = userCardCountMap.isEmpty() ? 0 : 
                userCardCountMap.values().stream().mapToInt(Integer::intValue).sum() / (double) userCardCountMap.size();
        
        result.put("averageCardsPerUserThisMonth", Math.round(averageCardsPerUser * 100.0) / 100.0);
        
        // Get previous month stats for comparison
        YearMonth previousMonth = currentMonth.minusMonths(1);
        LocalDate startOfPreviousMonth = previousMonth.atDay(1);
        LocalDate endOfPreviousMonth = previousMonth.atEndOfMonth();
        
        List<UserDailyCardStat> previousMonthStats = userDailyCardStatRepository.findAll().stream()
                .filter(stat -> !stat.getStatDate().isBefore(startOfPreviousMonth) && !stat.getStatDate().isAfter(endOfPreviousMonth))
                .collect(Collectors.toList());
        
        Map<Integer, Integer> prevUserCardCountMap = new HashMap<>();
        for (UserDailyCardStat stat : previousMonthStats) {
            prevUserCardCountMap.put(stat.getUserId(), 
                    prevUserCardCountMap.getOrDefault(stat.getUserId(), 0) + stat.getCardCount());
        }
        
        double prevAverageCardsPerUser = prevUserCardCountMap.isEmpty() ? 0 : 
                prevUserCardCountMap.values().stream().mapToInt(Integer::intValue).sum() / (double) prevUserCardCountMap.size();
        
        double cardPercentChange = prevAverageCardsPerUser == 0 ? 100 : 
                ((averageCardsPerUser - prevAverageCardsPerUser) / prevAverageCardsPerUser) * 100;
        
        result.put("prevMonthAverageCardsPerUser", Math.round(prevAverageCardsPerUser * 100.0) / 100.0);
        result.put("cardPercentChange", Math.round(cardPercentChange * 100.0) / 100.0);
        
        // Get new users count in current month
        List<User> newUsersThisMonth = userRepository.findAll().stream()
                .filter(user -> user.getCreateDate() != null && 
                        !user.getCreateDate().isBefore(startOfCurrentMonth.atStartOfDay()) && 
                        !user.getCreateDate().isAfter(endOfCurrentMonth.atTime(23, 59, 59)))
                .collect(Collectors.toList());
        
        int newUsersCount = newUsersThisMonth.size();
        result.put("newUsersThisMonth", newUsersCount);
        
        // Get new users from previous month for comparison
        List<User> newUsersPrevMonth = userRepository.findAll().stream()
                .filter(user -> user.getCreateDate() != null && 
                        !user.getCreateDate().isBefore(startOfPreviousMonth.atStartOfDay()) && 
                        !user.getCreateDate().isAfter(endOfPreviousMonth.atTime(23, 59, 59)))
                .collect(Collectors.toList());
        
        int prevMonthNewUsers = newUsersPrevMonth.size();
        double userPercentChange = prevMonthNewUsers == 0 ? 100 : 
                ((newUsersCount - prevMonthNewUsers) / (double) prevMonthNewUsers) * 100;
        
        result.put("prevMonthNewUsers", prevMonthNewUsers);
        result.put("userPercentChange", Math.round(userPercentChange * 100.0) / 100.0);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getDailyActiveUsers(LocalDate startDate, LocalDate endDate) {
        // If dates are not provided, use the last 30 days
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(29);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        // Get all study logs in the date range
        List<StudyLog> logs = studyLogRepository.findAll().stream()
                .filter(log -> log.getUpdateDate() != null && 
                       !log.getUpdateDate().isBefore(startDateTime) && 
                       !log.getUpdateDate().isAfter(endDateTime))
                .collect(Collectors.toList());
        
        // Group by date and count distinct users
        Map<LocalDate, Set<Integer>> usersByDate = new HashMap<>();
        
        // Process each log and group users by date
        for (StudyLog log : logs) {
            LocalDate logDate = log.getUpdateDate().toLocalDate();
            usersByDate.computeIfAbsent(logDate, k -> new HashSet<>()).add(log.getUserId());
        }
        
        // Convert to count map
        Map<LocalDate, Long> dailyActiveUsersMap = new HashMap<>();
        usersByDate.forEach((date, users) -> dailyActiveUsersMap.put(date, (long) users.size()));
        
        // Generate result for all days in range
        List<Map<String, Object>> result = new ArrayList<>();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        for (int i = 0; i < daysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", currentDate.toString());
            dayData.put("activeUsers", dailyActiveUsersMap.getOrDefault(currentDate, 0L));
            result.add(dayData);
        }
        
        return result;
    }

    @Override
    public Map<String, Integer> getQuestionTypeDistribution() {
        List<Card> allCards = cardRepository.findAll();
        
        // Count cards by type
        Map<String, Integer> distribution = new HashMap<>();
        
        for (CardType type : CardType.values()) {
            long count = allCards.stream()
                    .filter(card -> card.getType() == type)
                    .count();
            
            distribution.put(type.name(), (int) count);
        }
        
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getRecentUsers() {
        // Get 4 most recent users
        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createDate"));
        List<User> recentUsers = userRepository.findAll(pageable).getContent();
        
        return recentUsers.stream().map(user -> {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("avatar", user.getAvatar());
            userData.put("createDate", user.getCreateDate());
            return userData;
        }).collect(Collectors.toList());
    }
} 