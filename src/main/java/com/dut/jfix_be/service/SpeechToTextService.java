package com.dut.jfix_be.service;

import java.util.Map;

public interface SpeechToTextService {
    String speechToText(String base64Audio, String language);
    Map<String, Object> checkSpeech(String audioData, String userRomaji, String language);
} 