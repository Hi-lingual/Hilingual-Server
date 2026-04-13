package org.sopt;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.sopt.aws.config.AWSProperties;
import org.sopt.openai.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.sopt.jwt.auth.domain.TokenRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "org.sopt",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = TokenRepository.class
        )
)
@EnableRedisRepositories(basePackages = "org.sopt.jwt.auth.domain")
@EnableConfigurationProperties({AWSProperties.class, OpenAIProperties.class})
public class HilingualApplication {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

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
