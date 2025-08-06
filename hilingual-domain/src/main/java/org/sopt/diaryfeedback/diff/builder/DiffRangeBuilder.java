package org.sopt.diaryfeedback.diff.builder;

import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.diaryfeedback.diff.data.DiffOperation;
import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.sopt.diaryfeedback.diff.parser.TextParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiffRangeBuilder {

    private final TextParser textParser;

    public DiffRangeBuilder(TextParser textParser) {
        this.textParser = textParser;
    }

    public List<DiaryDetailsRes.DiffRange> buildDiffRanges(List<DiffOperation> operations, String rewriteText) {
        List<DiaryDetailsRes.DiffRange> diffRanges = new ArrayList<>();
        int rewriteIndex = 0; // rewrite 텍스트에서의 현재 위치

        for (DiffOperation operation : operations) {
            switch (operation.getType()) {
                case INSERT -> {
                    // 삽입된 단어의 실제 위치 찾기
                    WordInfo insertWord = operation.getRewriteWord();
                    int start = textParser.findWordPosition(rewriteText, insertWord.getCleanWord(), rewriteIndex);
                    int end = start + insertWord.getOriginalWord().length();

                    diffRanges.add(new DiaryDetailsRes.DiffRange(start, end, insertWord.getOriginalWord()));
                    rewriteIndex = end;
                }
                case EQUAL -> {
                    // 동일한 단어의 위치 업데이트
                    WordInfo equalWord = operation.getRewriteWord();
                    int wordStart = textParser.findWordPosition(rewriteText, equalWord.getCleanWord(), rewriteIndex);
                    rewriteIndex = wordStart + equalWord.getOriginalWord().length();
                }
                case DELETE -> {
                    // DELETE는 rewrite 텍스트에 영향 없음
                }
            }
        }

        return diffRanges;
    }
}
