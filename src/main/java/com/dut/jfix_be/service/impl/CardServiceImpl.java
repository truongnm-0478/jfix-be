package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.request.CardRequest;
import com.dut.jfix_be.dto.response.CardResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.Deck;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.DeckRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.CardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public CardResponse createCard(CardRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));

        Deck deck = deckRepository.findById(request.getDeckId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.deck.not.found", null, LocaleContextHolder.getLocale())
                ));

        if (!deck.getUserId().equals(user.getId())) {
            throw new ResourceNotFoundException(
                messageSource.getMessage("error.deck.not.found", null, LocaleContextHolder.getLocale())
            );
        }

        Card card = Card.builder()
                .deckId(request.getDeckId())
                .type(request.getType())
                .itemId(request.getItemId())
                .skill(request.getSkill())
                .createDate(LocalDateTime.now())
                .createBy(username)
                .build();

        card = cardRepository.save(card);
        return CardResponse.fromCard(card);
    }
} 