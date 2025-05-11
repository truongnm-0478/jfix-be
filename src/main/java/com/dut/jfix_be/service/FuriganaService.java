package com.dut.jfix_be.service;

public interface FuriganaService {
    String applyFurigana(String text);
    String toRomaji(String text);
    String cleanRomaji(String input);
}
