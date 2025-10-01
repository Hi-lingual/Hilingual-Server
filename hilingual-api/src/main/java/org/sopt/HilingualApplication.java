package org.sopt;

import io.github.cdimascio.dotenv.Dotenv;
import org.sopt.aws.config.AWSProperties;
import org.sopt.openai.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({AWSProperties.class, OpenAIProperties.class})
public class HilingualApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        String active = dotenv.get("SPRING_PROFILES_ACTIVE");
        if (active != null && !active.isBlank()) {
            System.setProperty("spring.profiles.active", active);
        }

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(HilingualApplication.class, args);
    }
}
