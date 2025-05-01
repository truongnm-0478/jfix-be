package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.request.DeckRequest;
import com.dut.jfix_be.dto.response.DeckResponse;
import com.dut.jfix_be.entity.Deck;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.DeckRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.DeckService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public DeckResponse createDeck(DeckRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        Deck deck = Deck.builder()
                .name(request.getName())
                .description(request.getDescription())
                .userId(user.getId())
                .createDate(LocalDateTime.now())
                .createBy(username)
                .build();

        deck = deckRepository.save(deck);
        return DeckResponse.fromDeck(deck);
    }

    @Override
    public List<DeckResponse> getUserDecks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        return deckRepository.findActiveByUserId(user.getId()).stream()
                .map(DeckResponse::fromDeck)
                .collect(Collectors.toList());
    }

    @Override
    public DeckResponse getDeckById(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.deck.not.found", null, LocaleContextHolder.getLocale())
                ));

        if (!deck.getUserId().equals(user.getId())) {
            throw new ResourceNotFoundException(
                messageSource.getMessage("error.deck.not.found", null, LocaleContextHolder.getLocale())
            );
        }

        return DeckResponse.fromDeck(deck);
    }
} 