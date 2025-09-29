package org.sopt.controller.discord.service;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class WebhookService {

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    private final UserFacade userFacade;

    public void sendNewUserNotification() {

        RestTemplate restTemplate = new RestTemplate();

        // 회원 수 조회
        final long totalMembers = userFacade.count();

        // 알림 메시지
        String message = totalMembers + "번째 유저가 로그인했습니다!\n";
        sendDiscordWebhook(message);
    }

    public void sendCompleteUserNotification(final long userId) {
        RestTemplate restTemplate = new RestTemplate();

        String message = userFacade.getUserById(userId).getUserProfile().getNickname() + "회원님이 회원가입을 완료했습니다!\n";
        sendDiscordWebhook(message);
    }

    private void sendDiscordWebhook(String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("content", message);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);
    }
}
