package com.dut.jfix_be.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.dut.jfix_be.service.FuriganaService;

@Service
public class FuriganaServiceImpl implements FuriganaService {
    private final Tokenizer tokenizer = new Tokenizer();
    private static final Map<String, String> KATAKANA_ROMAJI_MAP = createKatakanaRomajiMap();
    private static final Map<String, String> ROMAJI_HIRA_MAP = new HashMap<>();

    static {
        // Âm ghép 3 ký tự
        ROMAJI_HIRA_MAP.put("kyo", "きょ"); ROMAJI_HIRA_MAP.put("kyu", "きゅ"); ROMAJI_HIRA_MAP.put("kya", "きゃ");
        ROMAJI_HIRA_MAP.put("gyo", "ぎょ"); ROMAJI_HIRA_MAP.put("gyu", "ぎゅ"); ROMAJI_HIRA_MAP.put("gya", "ぎゃ");
        ROMAJI_HIRA_MAP.put("sho", "しょ"); ROMAJI_HIRA_MAP.put("shu", "しゅ"); ROMAJI_HIRA_MAP.put("sha", "しゃ");
        ROMAJI_HIRA_MAP.put("jo", "じょ"); ROMAJI_HIRA_MAP.put("ju", "じゅ"); ROMAJI_HIRA_MAP.put("ja", "じゃ");
        ROMAJI_HIRA_MAP.put("cho", "ちょ"); ROMAJI_HIRA_MAP.put("chu", "ちゅ"); ROMAJI_HIRA_MAP.put("cha", "ちゃ");
        ROMAJI_HIRA_MAP.put("nyo", "にょ"); ROMAJI_HIRA_MAP.put("nyu", "にゅ"); ROMAJI_HIRA_MAP.put("nya", "にゃ");
        ROMAJI_HIRA_MAP.put("hyo", "ひょ"); ROMAJI_HIRA_MAP.put("hyu", "ひゅ"); ROMAJI_HIRA_MAP.put("hya", "ひゃ");
        ROMAJI_HIRA_MAP.put("myo", "みょ"); ROMAJI_HIRA_MAP.put("myu", "みゅ"); ROMAJI_HIRA_MAP.put("mya", "みゃ");
        ROMAJI_HIRA_MAP.put("ryo", "りょ"); ROMAJI_HIRA_MAP.put("ryu", "りゅ"); ROMAJI_HIRA_MAP.put("rya", "りゃ");
        ROMAJI_HIRA_MAP.put("byo", "びょ"); ROMAJI_HIRA_MAP.put("byu", "びゅ"); ROMAJI_HIRA_MAP.put("bya", "びゃ");
        ROMAJI_HIRA_MAP.put("pyo", "ぴょ"); ROMAJI_HIRA_MAP.put("pyu", "ぴゅ"); ROMAJI_HIRA_MAP.put("pya", "ぴゃ");
        ROMAJI_HIRA_MAP.put("tsu", "つ"); ROMAJI_HIRA_MAP.put("shi", "し"); ROMAJI_HIRA_MAP.put("chi", "ち"); ROMAJI_HIRA_MAP.put("fu", "ふ");
        // Đơn âm
        ROMAJI_HIRA_MAP.put("a", "あ"); ROMAJI_HIRA_MAP.put("i", "い"); ROMAJI_HIRA_MAP.put("u", "う"); ROMAJI_HIRA_MAP.put("e", "え"); ROMAJI_HIRA_MAP.put("o", "お");
        ROMAJI_HIRA_MAP.put("ka", "か"); ROMAJI_HIRA_MAP.put("ki", "き"); ROMAJI_HIRA_MAP.put("ku", "く"); ROMAJI_HIRA_MAP.put("ke", "け"); ROMAJI_HIRA_MAP.put("ko", "こ");
        ROMAJI_HIRA_MAP.put("ga", "が"); ROMAJI_HIRA_MAP.put("gi", "ぎ"); ROMAJI_HIRA_MAP.put("gu", "ぐ"); ROMAJI_HIRA_MAP.put("ge", "げ"); ROMAJI_HIRA_MAP.put("go", "ご");
        ROMAJI_HIRA_MAP.put("sa", "さ"); ROMAJI_HIRA_MAP.put("su", "す"); ROMAJI_HIRA_MAP.put("se", "せ"); ROMAJI_HIRA_MAP.put("so", "そ");
        ROMAJI_HIRA_MAP.put("za", "ざ"); ROMAJI_HIRA_MAP.put("ji", "じ"); ROMAJI_HIRA_MAP.put("zu", "ず"); ROMAJI_HIRA_MAP.put("ze", "ぜ"); ROMAJI_HIRA_MAP.put("zo", "ぞ");
        ROMAJI_HIRA_MAP.put("ta", "た"); ROMAJI_HIRA_MAP.put("te", "て"); ROMAJI_HIRA_MAP.put("to", "と");
        ROMAJI_HIRA_MAP.put("da", "だ"); ROMAJI_HIRA_MAP.put("de", "で"); ROMAJI_HIRA_MAP.put("do", "ど");
        ROMAJI_HIRA_MAP.put("na", "な"); ROMAJI_HIRA_MAP.put("ni", "に"); ROMAJI_HIRA_MAP.put("nu", "ぬ"); ROMAJI_HIRA_MAP.put("ne", "ね"); ROMAJI_HIRA_MAP.put("no", "の");
        ROMAJI_HIRA_MAP.put("ha", "は"); ROMAJI_HIRA_MAP.put("hi", "ひ"); ROMAJI_HIRA_MAP.put("fu", "ふ"); ROMAJI_HIRA_MAP.put("he", "へ"); ROMAJI_HIRA_MAP.put("ho", "ほ");
        ROMAJI_HIRA_MAP.put("ba", "ば"); ROMAJI_HIRA_MAP.put("bi", "び"); ROMAJI_HIRA_MAP.put("bu", "ぶ"); ROMAJI_HIRA_MAP.put("be", "べ"); ROMAJI_HIRA_MAP.put("bo", "ぼ");
        ROMAJI_HIRA_MAP.put("pa", "ぱ"); ROMAJI_HIRA_MAP.put("pi", "ぴ"); ROMAJI_HIRA_MAP.put("pu", "ぷ"); ROMAJI_HIRA_MAP.put("pe", "ぺ"); ROMAJI_HIRA_MAP.put("po", "ぽ");
        ROMAJI_HIRA_MAP.put("ma", "ま"); ROMAJI_HIRA_MAP.put("mi", "み"); ROMAJI_HIRA_MAP.put("mu", "む"); ROMAJI_HIRA_MAP.put("me", "め"); ROMAJI_HIRA_MAP.put("mo", "も");
        ROMAJI_HIRA_MAP.put("ya", "や"); ROMAJI_HIRA_MAP.put("yu", "ゆ"); ROMAJI_HIRA_MAP.put("yo", "よ");
        ROMAJI_HIRA_MAP.put("ra", "ら"); ROMAJI_HIRA_MAP.put("ri", "り"); ROMAJI_HIRA_MAP.put("ru", "る"); ROMAJI_HIRA_MAP.put("re", "れ"); ROMAJI_HIRA_MAP.put("ro", "ろ");
        ROMAJI_HIRA_MAP.put("wa", "わ"); ROMAJI_HIRA_MAP.put("wo", "を");
        ROMAJI_HIRA_MAP.put("n", "ん");
    }

    private static Map<String, String> createKatakanaRomajiMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ア", "a"); map.put("イ", "i"); map.put("ウ", "u"); map.put("エ", "e"); map.put("オ", "o");
        map.put("カ", "ka"); map.put("キ", "ki"); map.put("ク", "ku"); map.put("ケ", "ke"); map.put("コ", "ko");
        map.put("サ", "sa"); map.put("シ", "shi"); map.put("ス", "su"); map.put("セ", "se"); map.put("ソ", "so");
        map.put("タ", "ta"); map.put("チ", "chi"); map.put("ツ", "tsu"); map.put("テ", "te"); map.put("ト", "to");
        map.put("ナ", "na"); map.put("ニ", "ni"); map.put("ヌ", "nu"); map.put("ネ", "ne"); map.put("ノ", "no");
        map.put("ハ", "ha"); map.put("ヒ", "hi"); map.put("フ", "fu"); map.put("ヘ", "he"); map.put("ホ", "ho");
        map.put("マ", "ma"); map.put("ミ", "mi"); map.put("ム", "mu"); map.put("メ", "me"); map.put("モ", "mo");
        map.put("ヤ", "ya"); map.put("ユ", "yu"); map.put("ヨ", "yo");
        map.put("ラ", "ra"); map.put("リ", "ri"); map.put("ル", "ru"); map.put("レ", "re"); map.put("ロ", "ro");
        map.put("ワ", "wa"); map.put("ヲ", "wo"); map.put("ン", "n");
        map.put("ガ", "ga"); map.put("ギ", "gi"); map.put("グ", "gu"); map.put("ゲ", "ge"); map.put("ゴ", "go");
        map.put("ザ", "za"); map.put("ジ", "ji"); map.put("ズ", "zu"); map.put("ゼ", "ze"); map.put("ゾ", "zo");
        map.put("ダ", "da"); map.put("ヂ", "ji"); map.put("ヅ", "zu"); map.put("デ", "de"); map.put("ド", "do");
        map.put("バ", "ba"); map.put("ビ", "bi"); map.put("ブ", "bu"); map.put("ベ", "be"); map.put("ボ", "bo");
        map.put("パ", "pa"); map.put("ピ", "pi"); map.put("プ", "pu"); map.put("ペ", "pe"); map.put("ポ", "po");
        map.put("ァ", "a"); map.put("ィ", "i"); map.put("ゥ", "u"); map.put("ェ", "e"); map.put("ォ", "o");
        map.put("ヴ", "vu");
        map.put("キャ", "kya"); map.put("キュ", "kyu"); map.put("キョ", "kyo");
        map.put("シャ", "sha"); map.put("シュ", "shu"); map.put("ショ", "sho");
        map.put("チャ", "cha"); map.put("チュ", "chu"); map.put("チョ", "cho");
        map.put("ニャ", "nya"); map.put("ニュ", "nyu"); map.put("ニョ", "nyo");
        map.put("ヒャ", "hya"); map.put("ヒュ", "hyu"); map.put("ヒョ", "hyo");
        map.put("ミャ", "mya"); map.put("ミュ", "myu"); map.put("ミョ", "myo");
        map.put("リャ", "rya"); map.put("リュ", "ryu"); map.put("リョ", "ryo");
        map.put("ギャ", "gya"); map.put("ギュ", "gyu"); map.put("ギョ", "gyo");
        map.put("ジャ", "ja"); map.put("ジュ", "ju"); map.put("ジョ", "jo");
        map.put("ビャ", "bya"); map.put("ビュ", "byu"); map.put("ビョ", "byo");
        map.put("ピャ", "pya"); map.put("ピュ", "pyu"); map.put("ピョ", "pyo");
        map.put("ッ", "");
        map.put("ー", "-");
        return map;
    }

    @Override
    public String applyFurigana(String text) {
        if (text == null)
            return null;
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

    @Override
    public String toRomaji(String text) {
        if (text == null)
            return null;
        List<Token> tokens = tokenizer.tokenize(text);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            String reading = token.getReading();
            if (reading != null) {
                sb.append(katakanaToRomaji(reading));
            } else {
                sb.append(token.getSurface());
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public String katakanaToRomaji(String katakana) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < katakana.length(); ) {
            if (i + 2 <= katakana.length()) {
                String chunk = katakana.substring(i, i + 2);
                if (KATAKANA_ROMAJI_MAP.containsKey(chunk)) {
                    sb.append(KATAKANA_ROMAJI_MAP.get(chunk));
                    i += 2;
                    continue;
                }
            }
            String chunk = katakana.substring(i, i + 1);
            String romaji = KATAKANA_ROMAJI_MAP.getOrDefault(chunk, chunk);
            if (chunk.equals("ッ") && i + 1 < katakana.length()) {
                String next;
                if (i + 3 <= katakana.length()) {
                    next = katakana.substring(i + 1, i + 3);
                    if (KATAKANA_ROMAJI_MAP.containsKey(next)) {
                        romaji = KATAKANA_ROMAJI_MAP.get(next).substring(0, 1);
                    } else {
                        next = katakana.substring(i + 1, i + 2);
                        romaji = KATAKANA_ROMAJI_MAP.getOrDefault(next, next).substring(0, 1);
                    }
                } else {
                    next = katakana.substring(i + 1, i + 2);
                    romaji = KATAKANA_ROMAJI_MAP.getOrDefault(next, next).substring(0, 1);
                }
                sb.append(romaji);
                i++;
                continue;
            }
            if (chunk.equals("ー") && sb.length() > 0) {
                char last = sb.charAt(sb.length() - 1);
                sb.append(last);
                i++;
                continue;
            }
            sb.append(romaji);
            i++;
        }
        return sb.toString();
    }

    @Override
    public String cleanRomaji(String input) {
        if (input == null) return null;
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    public List<Token> tokenize(String text) {
        return tokenizer.tokenize(text);
    }

    public String romajiToHiragana(String romaji) {
        if (romaji == null) return null;
        String r = romaji.toLowerCase();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < r.length()) {
            // Âm ghép 3 ký tự
            if (i + 3 <= r.length()) {
                String tri = r.substring(i, i + 3);
                if (ROMAJI_HIRA_MAP.containsKey(tri)) {
                    sb.append(ROMAJI_HIRA_MAP.get(tri));
                    i += 3;
                    continue;
                }
            }
            // Âm ghép 2 ký tự
            if (i + 2 <= r.length()) {
                String bi = r.substring(i, i + 2);
                if (ROMAJI_HIRA_MAP.containsKey(bi)) {
                    sb.append(ROMAJI_HIRA_MAP.get(bi));
                    i += 2;
                    continue;
                }
            }
            // Âm đơn 1 ký tự
            String single = r.substring(i, i + 1);
            if (ROMAJI_HIRA_MAP.containsKey(single)) {
                sb.append(ROMAJI_HIRA_MAP.get(single));
            }
            // Nếu không khớp, bỏ qua hoặc thêm ký tự đặc biệt
            i++;
        }
        return sb.toString();
    }

    public String toHiragana(String text) {
        if (text == null) return null;
        List<Token> tokens = tokenizer.tokenize(text);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            String reading = token.getReading();
            if (reading != null) {
                sb.append(katakanaToHiragana(reading));
            } else {
                sb.append(token.getSurface());
            }
        }
        return sb.toString();
    }
}
