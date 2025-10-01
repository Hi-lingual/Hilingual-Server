package org.sopt.controller.discord.service;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
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

    private final UserProfileFacade userProfileFacade;

    public void sendCompleteUserNotification(UserProfile userProfile) {
        final long totalMembers = userProfileFacade.count();

        String message = "✏️하이링구얼 신규 회원✏️\n🔸닉네임 : " + userProfile.getNickname() + "\n🔸현재 가입 유저 수 : " + totalMembers + "명\n🔸플랫폼 : " + userProfile.getUser().getProvider();
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
