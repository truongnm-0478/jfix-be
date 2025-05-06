package com.dut.jfix_be.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.GrammarRequest;
import com.dut.jfix_be.dto.response.GrammarAdminResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.entity.UserMistake;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.CorrectionHistoryRepository;
import com.dut.jfix_be.repository.GrammarRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
import com.dut.jfix_be.repository.UserErrorAnalyticsRepository;
import com.dut.jfix_be.repository.UserMistakeRepository;
import com.dut.jfix_be.service.GrammarService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GrammarServiceImpl implements GrammarService {

    private final GrammarRepository grammarRepository;
    private final CardRepository cardRepository;
    private final StudyLogRepository studyLogRepository;
    private final UserMistakeRepository userMistakeRepository;
    private final UserErrorAnalyticsRepository userErrorAnalyticsRepository;
    private final CorrectionHistoryRepository correctionHistoryRepository;

    @Override
    public List<GrammarResponse> findByLevel(JlptLevel level) {
        List<Grammar> grammars = grammarRepository.findByLevel(level);
        return grammars.stream()
                .map(GrammarResponse::fromGrammar)
                .collect(Collectors.toList());
    }

    @Override
    public DataWithPageResponse<GrammarResponse> getGrammarsByLevelWithLimit(JlptLevel level, int numberOfRecords, int page) {
        if (level == null || numberOfRecords <= 0 || page < 0) {
            return DataWithPageResponse.<GrammarResponse>builder()
                    .data(Collections.emptyList())
                    .totalRecords(0L)
                    .totalPages(0)
                    .nextPage(null)
                    .previousPage(null)
                    .build();
        }

        long totalRecords = grammarRepository.countByLevel(level);
        int totalPages = (int) Math.ceil((double) totalRecords / numberOfRecords);
        Pageable pageable = PageRequest.of(page, numberOfRecords);

        List<GrammarResponse> grammars = grammarRepository.findByLevelWithPageable(level, pageable)
                .getContent()
                .stream()
                .map(GrammarResponse::fromGrammar)
                .collect(Collectors.toList());

        Integer nextPage = (page < totalPages - 1) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;

        return DataWithPageResponse.<GrammarResponse>builder()
                .data(grammars)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public GrammarResponse findById(Integer id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new com.dut.jfix_be.exception.ResourceNotFoundException("error.grammar.not.found", id));
        return GrammarResponse.fromGrammar(grammar);
    }

    @Override
    public DataWithPageResponse<GrammarResponse> getAllGrammars(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<Grammar> grammars = grammarRepository.findAll();
        List<GrammarResponse> filtered = grammars.stream()
                .filter(g -> (keyword == null || g.getStructure().toLowerCase().contains(keyword.toLowerCase()) || (g.getMeaning() != null && g.getMeaning().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(g -> (level == null || g.getLevel() == level))
                .sorted((g1, g2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "structure": cmp = g1.getStructure().compareToIgnoreCase(g2.getStructure()); break;
                        case "meaning": cmp = g1.getMeaning() != null && g2.getMeaning() != null ? g1.getMeaning().compareToIgnoreCase(g2.getMeaning()) : 0; break;
                        case "level": cmp = g1.getLevel().name().compareTo(g2.getLevel().name()); break;
                        case "id": default: cmp = g1.getId().compareTo(g2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(GrammarResponse::fromGrammar)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<GrammarResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<GrammarResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public DataWithPageResponse<GrammarAdminResponse> getAllGrammarsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir) {
        List<Grammar> grammars = grammarRepository.findAll();
        List<GrammarAdminResponse> filtered = grammars.stream()
                .filter(g -> (keyword == null || g.getStructure().toLowerCase().contains(keyword.toLowerCase()) || (g.getMeaning() != null && g.getMeaning().toLowerCase().contains(keyword.toLowerCase()))))
                .filter(g -> (level == null || g.getLevel() == level))
                .sorted((g1, g2) -> {
                    int cmp = 0;
                    switch (sortBy) {
                        case "structure": cmp = g1.getStructure().compareToIgnoreCase(g2.getStructure()); break;
                        case "meaning": cmp = g1.getMeaning() != null && g2.getMeaning() != null ? g1.getMeaning().compareToIgnoreCase(g2.getMeaning()) : 0; break;
                        case "level": cmp = g1.getLevel().name().compareTo(g2.getLevel().name()); break;
                        case "id": default: cmp = g1.getId().compareTo(g2.getId()); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? -cmp : cmp;
                })
                .map(GrammarAdminResponse::fromGrammar)
                .toList();
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<GrammarAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<GrammarAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public GrammarAdminResponse getGrammarDetailForAdmin(Integer id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new com.dut.jfix_be.exception.ResourceNotFoundException("error.grammar.not.found", id));
        return GrammarAdminResponse.fromGrammar(grammar);
    }

    @Override
    public GrammarAdminResponse createGrammarForAdmin(GrammarRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Grammar grammar = new Grammar();
        grammar.setRomaji(request.getRomaji());
        grammar.setStructure(request.getStructure());
        grammar.setUsage(request.getUsage());
        grammar.setMeaning(request.getMeaning());
        grammar.setExample(request.getExample());
        grammar.setExampleMeaning(request.getExampleMeaning());
        grammar.setLevel(request.getLevel());
        grammar.setCreateDate(java.time.LocalDateTime.now());
        grammar.setCreateBy(adminUsername);
        grammarRepository.save(grammar);
        return GrammarAdminResponse.fromGrammar(grammar);
    }

    @Override
    public GrammarAdminResponse updateGrammarForAdmin(Integer id, GrammarRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth != null ? auth.getName() : "admin";
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new com.dut.jfix_be.exception.ResourceNotFoundException("error.grammar.not.found", id));
        grammar.setRomaji(request.getRomaji());
        grammar.setStructure(request.getStructure());
        grammar.setUsage(request.getUsage());
        grammar.setMeaning(request.getMeaning());
        grammar.setExample(request.getExample());
        grammar.setExampleMeaning(request.getExampleMeaning());
        grammar.setLevel(request.getLevel());
        grammar.setUpdateDate(java.time.LocalDateTime.now());
        grammar.setUpdateBy(adminUsername);
        grammarRepository.save(grammar);
        return GrammarAdminResponse.fromGrammar(grammar);
    }

    @Override
    public void deleteGrammarForAdmin(Integer id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new com.dut.jfix_be.exception.ResourceNotFoundException("error.grammar.not.found", id));
        List<Card> cards = cardRepository.findByTypeAndItemId(CardType.GRAMMAR, id);
        for (Card card : cards) {
            studyLogRepository.deleteAllByCardId(card.getId());
            List<UserMistake> mistakes = userMistakeRepository.findByCardId(card.getId());
            for (UserMistake mistake : mistakes) {
                correctionHistoryRepository.deleteAllByUserMistakeId(mistake.getId());
            }
            userMistakeRepository.deleteAllByCardId(card.getId());
            userErrorAnalyticsRepository.deleteAllByCardId(card.getId());
            cardRepository.delete(card);
        }
        grammarRepository.delete(grammar);
    }
}
