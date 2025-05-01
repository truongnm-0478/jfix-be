package com.dut.jfix_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckRequest {
    @NotBlank(message = "error.deck.name.required")
    private String name;
    
    private String description;
} 