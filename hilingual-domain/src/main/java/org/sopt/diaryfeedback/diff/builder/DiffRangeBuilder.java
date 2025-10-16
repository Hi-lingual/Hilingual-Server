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

    public List<DiaryDetailsRes.DiffRange> buildDiffRanges(List<DiffOperation> operations, String rewriteText) {
        List<DiaryDetailsRes.DiffRange> out = new ArrayList<>();

        DiaryDetailsRes.DiffRange cur = null;

        for (DiffOperation op : operations) {
            if (op.getType() == DiffType.INSERT) {
                WordInfo w = op.getRewriteWord();
                if (w == null) continue;

                int s = w.getStart();
                int e = w.getEnd();
                String t = w.getOriginalWord();

                if (cur == null) {
                    cur = new DiaryDetailsRes.DiffRange(s, e, t);
                } else if (cur.end() == s) {
                    cur = new DiaryDetailsRes.DiffRange(
                            cur.start(), e, cur.correctedText() + t
                    );
                } else if (s - cur.end() == 1 && rewriteText.charAt(cur.end()) == ' ') {
                    cur = new DiaryDetailsRes.DiffRange(
                            cur.start(), e, cur.correctedText() + " " + t
                    );
                } else {
                    out.add(cur);
                    cur = new DiaryDetailsRes.DiffRange(s, e, t);
                }
            } else {
                if (cur != null) {
                    out.add(cur);
                    cur = null;
                }
            }
        }
        if (cur != null) out.add(cur);

        final int L = rewriteText.length();
        List<DiaryDetailsRes.DiffRange> valid = new ArrayList<>();

        for (DiaryDetailsRes.DiffRange r : out) {
            if (r.start() < 0 || r.end() > L || r.start() >= r.end()) {
                log.warn("[DiffRangeBuilder] Invalid range detected: [{} , {}) (rewrite length = {})",
                        r.start(), r.end(), L);
                continue;
            }
            valid.add(r);
        }

        return valid;
    }
}