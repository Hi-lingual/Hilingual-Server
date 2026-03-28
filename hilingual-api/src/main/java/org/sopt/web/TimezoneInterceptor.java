package org.sopt.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.sopt.context.TimezoneContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TimezoneInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String timezone = request.getHeader("X-Timezone");

        if (StringUtils.hasText(timezone)) {
            TimezoneContextHolder.setTimezone(timezone);
        } else {
            TimezoneContextHolder.setTimezone("Asia/Seoul"); // 헤더가 없을 경우
        }

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        TimezoneContextHolder.clear();
    }
}