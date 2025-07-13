package org.hilingual.auth.core.domain;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.Getter;

@Getter
public class GoogleOAuth2UserInfo {
    private final String id;

    public GoogleOAuth2UserInfo(GoogleIdToken.Payload payload) {
        this.id = payload.getSubject();
    }
}
