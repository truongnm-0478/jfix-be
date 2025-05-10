package com.dut.jfix_be.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.dut.jfix_be.service.FuriganaService;

@Service
public class FuriganaServiceImpl implements FuriganaService {
    private final Tokenizer tokenizer = new Tokenizer();

    @Override
    public String applyFurigana(String text) {
        if (text == null) return null;
        List<Token> tokens = tokenizer.tokenize(text);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            String surface = token.getSurface();
            String reading = token.getReading();

            if (reading != null) {
                reading = katakanaToHiragana(reading);
            }

            if (reading != null && containsKanji(surface)) {
                sb.append("<ruby>").append(surface)
                  .append("<rt>").append(reading).append("</rt>")
                  .append("</ruby>");
            } else {
                sb.append(surface);
            }
        }
        return sb.toString();
    }

    private boolean containsKanji(String text) {
        return text != null && text.codePoints().anyMatch(c -> (c >= 0x4e00 && c <= 0x9faf));
    }

    private String katakanaToHiragana(String katakana) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < katakana.length(); i++) {
            char c = katakana.charAt(i);
            if (c >= 'ア' && c <= 'ン') {
                sb.append((char) (c - ('ア' - 'あ')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
