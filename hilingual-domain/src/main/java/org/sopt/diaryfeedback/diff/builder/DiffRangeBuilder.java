package org.sopt.diaryfeedback.diff.builder;

import lombok.extern.slf4j.Slf4j;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.diaryfeedback.diff.data.DiffOperation;
import org.sopt.diaryfeedback.diff.data.DiffType;
import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class DiffRangeBuilder {

    public List<DiaryDetailsRes.DiffRange> buildDiffRanges(
            List<DiffOperation> operations,
            String rewriteText
    ) {
        List<DiaryDetailsRes.DiffRange> rawRanges = buildRawRanges(operations, rewriteText);
        return filterInvalidRanges(rawRanges, rewriteText.length());
    }

    private List<DiaryDetailsRes.DiffRange> buildRawRanges(
            List<DiffOperation> operations,
            String rewriteText
    ) {
        List<DiaryDetailsRes.DiffRange> ranges = new ArrayList<>();
        DiaryDetailsRes.DiffRange current = null;

        for (DiffOperation op : operations) {
            if (op.type() == DiffType.INSERT) {
                current = mergeInsertOperation(op, current, ranges, rewriteText);
                continue;
            }

            current = flushCurrentRange(current, ranges);
        }

        flushCurrentRange(current, ranges);
        return ranges;
    }

    private DiaryDetailsRes.DiffRange mergeInsertOperation(
            DiffOperation op,
            DiaryDetailsRes.DiffRange current,
            List<DiaryDetailsRes.DiffRange> ranges,
            String rewriteText
    ) {
        WordInfo word = op.rewriteWord();
        if (word == null) {
            return current;
        }

        int start = word.start();
        int end = word.end();
        String text = word.originalWord();

        if (current == null) {
            return new DiaryDetailsRes.DiffRange(start, end, text);
        }

        // 바로 붙어 있는 경우 -> 그대로 이어 붙임
        if (current.end() == start) {
            return new DiaryDetailsRes.DiffRange(
                    current.start(),
                    end,
                    current.correctedText() + text
            );
        }

        // 사이에 공백 1칸 있는 경우 ->  공백 포함해서 병합
        if (start - current.end() == 1 && rewriteText.charAt(current.end()) == ' ') {
            return new DiaryDetailsRes.DiffRange(
                    current.start(),
                    end,
                    current.correctedText() + " " + text
            );
        }

        // 그 외 -> 이전 구간을 확정하고, 새 구간 시작
        ranges.add(current);
        return new DiaryDetailsRes.DiffRange(start, end, text);
    }

    private DiaryDetailsRes.DiffRange flushCurrentRange(
            DiaryDetailsRes.DiffRange current,
            List<DiaryDetailsRes.DiffRange> ranges
    ) {
        if (current != null) {
            ranges.add(current);
        }
        return null;
    }

    private List<DiaryDetailsRes.DiffRange> filterInvalidRanges(
            List<DiaryDetailsRes.DiffRange> ranges,
            int rewriteLength
    ) {
        List<DiaryDetailsRes.DiffRange> valid = new ArrayList<>(ranges.size());

        for (DiaryDetailsRes.DiffRange r : ranges) {
            if (r.start() < 0 || r.end() > rewriteLength || r.start() >= r.end()) {
                log.warn(
                        "[DiffRangeBuilder] Invalid range detected: [{} , {}) (rewrite length = {})",
                        r.start(), r.end(), rewriteLength
                );
                continue;
            }
            valid.add(r);
        }

        return valid;
    }
}
