package org.sopt.firebase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    private boolean enabled = false;
    private String serviceAccountPath;
}
