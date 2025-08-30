package org.sopt.controller.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public record GoogleOAuth2UserInfo(String id) {

    public GoogleOAuth2UserInfo(GoogleIdToken.Payload payload) {
        this(payload.getSubject());
    }
}