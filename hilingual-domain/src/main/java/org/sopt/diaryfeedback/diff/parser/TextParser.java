package org.sopt.diaryfeedback.diff.parser;

import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\S+");
    private static final Pattern LEADING_PUNCT = Pattern.compile("^[\\p{Punct}]+");
    private static final Pattern TRAILING_PUNCT = Pattern.compile("[\\p{Punct}]+$");

    public List<WordInfo> extractWordsWithPosition(String raw) {
        String text = Normalizer.normalize(raw, Normalizer.Form.NFC);

        List<WordInfo> words = new ArrayList<>();
        Matcher matcher = WORD_PATTERN.matcher(text);

        while (matcher.find()) {
            String original = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            String clean = LEADING_PUNCT.matcher(original).replaceAll("");
            clean = TRAILING_PUNCT.matcher(clean).replaceAll("");

            boolean hasPunctuation = !original.equals(clean);
            words.add(new WordInfo(clean, original, start, end, hasPunctuation));
        }
        return words;
    }
}