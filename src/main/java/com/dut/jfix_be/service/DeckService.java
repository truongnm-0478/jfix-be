package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.request.DeckRequest;
import com.dut.jfix_be.dto.response.DeckResponse;

public interface DeckService {
    DeckResponse createDeck(DeckRequest request);
    List<DeckResponse> getUserDecks();
    DeckResponse getDeckById(Integer id);
} 