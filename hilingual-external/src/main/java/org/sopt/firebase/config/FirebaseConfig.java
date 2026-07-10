package org.sopt.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.firebase.FCMClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Firebase 초기화 설정.
 * firebase.enabled=true일 때만 활성화되며, 비활성 시 FirebaseMessaging Bean이 생성되지 않아
 * FCMClient(@ConditionalOnBean)도 함께 비활성화된다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "firebase", name = "enabled", havingValue = "true")
public class FirebaseConfig {

    private final FirebaseProperties firebaseProperties;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // 심볼릭 링크 해소를 위해 canonical path로 정규화
            File serviceAccountFile = new File(firebaseProperties.getServiceAccountPath()).getCanonicalFile();

            if (!serviceAccountFile.exists()) {
                log.error("Firebase 서비스 계정 파일이 존재하지 않습니다: path={}", serviceAccountFile.getPath());
                throw new IllegalStateException("Firebase 서비스 계정 파일이 존재하지 않습니다.");
            }

            try (FileInputStream serviceAccount = new FileInputStream(serviceAccountFile)) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp 초기화 완료");
            }
        }
        return FirebaseMessaging.getInstance();
    }

    @Bean
    public FCMClient fcmClient(FirebaseMessaging firebaseMessaging) {
        return new FCMClient(firebaseMessaging);
    }
}
