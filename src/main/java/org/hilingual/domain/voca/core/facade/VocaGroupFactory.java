package org.hilingual.domain.voca.core.facade;

import org.hilingual.domain.voca.api.dto.res.VocaItemResponse;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.api.dto.res.WordGroup;
import org.hilingual.domain.voca.api.exception.VocaInvalidSortTypeException;
import org.hilingual.domain.voca.core.domain.Voca;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class VocaGroupFactory {

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    public VocaListResponse create(final List<Voca> vocas, final int sort) {
        return switch (sort) {
            case 1 -> groupByFirstLetter(vocas);
            case 2 -> groupByDateGroup(vocas);
            default -> throw new VocaInvalidSortTypeException();
        };
    }

    private VocaListResponse groupByFirstLetter(final List<Voca> vocas) {
        List<VocaItemResponse> responses = vocas.stream()
                .map(VocaItemResponse::from)
                .toList();

        Map<String, List<VocaItemResponse>> grouped = new TreeMap<>();

        for (VocaItemResponse response : responses) {
            String key = response.phrase().substring(0, 1).toUpperCase();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
        }

        List<WordGroup> wordGroups = grouped.entrySet().stream()
                .map(entry -> new WordGroup(entry.getKey(), entry.getValue()))
                .toList();

        return new VocaListResponse(responses.size(), wordGroups);
    }

    private VocaListResponse groupByDateGroup(final List<Voca> vocas) {
        LocalDate today = LocalDate.now(SEOUL_ZONE);
        LocalDateTime todayStart = LocalDateTime.of(today, java.time.LocalTime.MIDNIGHT);
        LocalDateTime sevenDaysAgo = today.minusDays(6).atStartOfDay();
        LocalDateTime thirtyDaysAgo = today.minusDays(29).atStartOfDay();

        Map<String, List<VocaItemResponse>> grouped = new LinkedHashMap<>();
        grouped.put("today", new ArrayList<>());
        grouped.put("7days", new ArrayList<>());
        grouped.put("30days", new ArrayList<>());

        Map<String, List<VocaItemResponse>> monthlyGroups = new LinkedHashMap<>();
        Map<String, List<VocaItemResponse>> yearlyGroups = new LinkedHashMap<>();

        for (Voca voca : vocas) {
            LocalDateTime createdAt = voca.getCreatedAt();
            VocaItemResponse response = VocaItemResponse.from(voca);

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

        List<WordGroup> wordGroups = new ArrayList<>();

        grouped.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new WordGroup(key, list));
        });

        monthlyGroups.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new WordGroup(key, list));
        });

        yearlyGroups.forEach((key, list) -> {
            if (!list.isEmpty()) wordGroups.add(new WordGroup(key, list));
        });

        return new VocaListResponse(vocas.size(), wordGroups);
    }
}
