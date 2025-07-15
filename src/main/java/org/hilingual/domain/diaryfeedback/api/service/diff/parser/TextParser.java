package org.hilingual.domain.diaryfeedback.api.service.diff.parser;

import org.hilingual.domain.diaryfeedback.api.service.diff.data.WordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextParser {
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

    public int findWordPosition(String text, String word, int startFrom) {
        // startFrom 위치부터 단어를 찾기
        int pos = startFrom;

        // 공백 건너뛰기
        while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) {
            pos++;
        }

        // 단어 매칭 확인
        if (pos + word.length() <= text.length()) {
            String foundWord = text.substring(pos, pos + word.length());
            if (foundWord.equals(word)) {
                return pos;
            }
        }

        // 정확한 매칭이 안되면 indexOf 사용 (fallback)
        int foundPos = text.indexOf(word, pos);
        return foundPos != -1 ? foundPos : pos;
    }
}
