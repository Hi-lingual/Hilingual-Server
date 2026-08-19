package org.sopt.config;

import lombok.RequiredArgsConstructor;
import org.sopt.web.TimezoneInterceptor;
import org.sopt.web.UserIdOrNullArgumentResolver;
import org.sopt.web.UserTimezoneArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TimezoneInterceptor timezoneInterceptor;
    private final UserTimezoneArgumentResolver userTimezoneArgumentResolver;
    private final UserIdOrNullArgumentResolver userIdOrNullArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timezoneInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userTimezoneArgumentResolver);
        resolvers.add(userIdOrNullArgumentResolver);
    }
}