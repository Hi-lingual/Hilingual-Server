package org.sopt.voca.facade;

import org.sopt.voca.domain.Voca;
import org.sopt.voca.dto.VocaItemRes;
import org.sopt.voca.dto.VocaListRes;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class VocaGroupFactory {

    public VocaListRes createByDateGroup(final List<Voca> vocas) {
        return groupByDateGroup(vocas);
    }

    public VocaListRes createByFirstLetter(final List<Voca> vocas) {
        return groupByFirstLetter(vocas);
    }

    private VocaListRes groupByFirstLetter(final List<Voca> vocas) {
        List<VocaItemRes> responses = vocas.stream()
                .map(VocaItemRes::from)
                .toList();

        Map<String, List<VocaItemRes>> grouped = new TreeMap<>();

        for (VocaItemRes response : responses) {
            String key = response.phrase().substring(0, 1).toUpperCase();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
        }

        List<VocaListRes.WordGroup> wordGroups = grouped.entrySet().stream()
                .map(entry -> new VocaListRes.WordGroup(entry.getKey(), entry.getValue()))
                .toList();

        return new VocaListRes(responses.size(), wordGroups);
    }

    private VocaListRes groupByDateGroup(final List<Voca> vocas) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime sevenDaysAgo = today.minusDays(6).atStartOfDay();
        LocalDateTime thirtyDaysAgo = today.minusDays(29).atStartOfDay();

        Map<String, List<VocaItemRes>> grouped = new LinkedHashMap<>();
        grouped.put("today", new ArrayList<>());
        grouped.put("7days", new ArrayList<>());
        grouped.put("30days", new ArrayList<>());

        Map<String, List<VocaItemRes>> monthlyGroups = new LinkedHashMap<>();
        Map<String, List<VocaItemRes>> yearlyGroups = new LinkedHashMap<>();

        for (Voca voca : vocas) {
            LocalDateTime createdAt = voca.getCreatedAt();
            VocaItemRes response = VocaItemRes.from(voca);

            if (!createdAt.isBefore(todayStart)) {
                grouped.get("today").add(response);
            } else if (!createdAt.isBefore(sevenDaysAgo)) {
                grouped.get("7days").add(response);
            } else if (!createdAt.isBefore(thirtyDaysAgo)) {
                grouped.get("30days").add(response);
            } else {
                int month = createdAt.getMonthValue();
                int year = createdAt.getYear();
                String key = (year == today.getYear()) ? (month + "월") : String.valueOf(year);

                if (year == today.getYear()) {
                    monthlyGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
                } else {
                    yearlyGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
                }
            }
        }

        List<VocaListRes.WordGroup> wordGroups = new ArrayList<>();

        grouped.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new VocaListRes.WordGroup(key, list));
        });

        monthlyGroups.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new VocaListRes.WordGroup(key, list));
        });

        yearlyGroups.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new VocaListRes.WordGroup(key, list));
        });

        return new VocaListRes(vocas.size(), wordGroups);
    }
}
