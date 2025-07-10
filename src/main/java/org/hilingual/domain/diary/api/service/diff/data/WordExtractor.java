package org.hilingual.domain.diary.api.service.diff.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordExtractor {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\S+");

    public List<WordInfo> extractWordsWithPosition(String text) {
        List<WordInfo> words = new ArrayList<>();
        Matcher matcher = WORD_PATTERN.matcher(text);

        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            String cleanWord = word.replaceAll("[.,!?;:]", "");
            boolean hasPunctuation = !word.equals(cleanWord);
            words.add(new WordInfo(cleanWord, word, start, end, hasPunctuation));
        }

        return words;
    }
}