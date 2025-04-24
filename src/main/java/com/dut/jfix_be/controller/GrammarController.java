package com.dut.jfix_be.controller;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.GrammarListResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.GrammarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grammars")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<ApiResponse<List<GrammarResponse>>> getGrammarsByLevel(@PathVariable JlptLevel level) {
        List<GrammarResponse> grammars = grammarService.findByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(grammars));
    }

    @GetMapping("/level/{level}/limit")
    public ResponseEntity<ApiResponse<DataWithPageResponse<GrammarResponse>>> getGrammarsByLevelWithLimit(
            @PathVariable JlptLevel level,
            @RequestParam("limit") int numberOfRecords,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        DataWithPageResponse<GrammarResponse> result = grammarService.getGrammarsByLevelWithLimit(level, numberOfRecords, page);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
