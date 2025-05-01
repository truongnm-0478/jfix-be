package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.CardRequest;
import com.dut.jfix_be.dto.response.CardResponse;

public interface CardService {
    CardResponse createCard(CardRequest request);
}