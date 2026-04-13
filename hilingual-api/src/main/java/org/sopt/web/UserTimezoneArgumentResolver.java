package org.sopt.web;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.sopt.annotation.UserTimezone;
import org.sopt.context.TimezoneContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import java.time.ZoneId;

@Component
public class UserTimezoneArgumentResolver implements HandlerMethodArgumentResolver {

    // 어떤 파라미터에 이 Resolver를 적용할 것인지?
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(UserTimezone.class);
        boolean isZoneIdType = ZoneId.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isZoneIdType;
    }

    // 파라미터에 실제로 주입할 값
    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        // Interceptor가 파싱해서 ThreadLocal에 넣어둔 타임존을 반환
        return TimezoneContextHolder.getTimezone();
    }
}