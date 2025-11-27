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
    private static final Pattern PREFIX_PUNCT_PATTERN = Pattern.compile("^[\\p{Punct}]+");
    private static final Pattern SUFFIX_PUNCT_PATTERN = Pattern.compile("[\\p{Punct}]+$");

    public List<WordInfo> extractWordsWithPosition(String rawText) {
        String normalized = Normalizer.normalize(rawText, Normalizer.Form.NFC);

        List<WordInfo> words = new ArrayList<>();
        Matcher m = WORD_PATTERN.matcher(normalized);

        while (m.find()) {
            String original = m.group();
            String clean = clean(original);
            words.add(new WordInfo(
                    clean,
                    original,
                    m.start(),
                    m.end(),
                    !original.equals(clean)
            ));
        }
        return words;
    }

    private String clean(String word) {
        return SUFFIX_PUNCT_PATTERN.matcher(
                PREFIX_PUNCT_PATTERN.matcher(word).replaceAll("")).replaceAll("");
    }
}
