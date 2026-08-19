package org.sopt.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
인증이 선택적인 엔드포인트용.
유효한 access token이 있으면 userId를, 없으면(익명/만료) null을 주입한다.
*/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIdOrNull {

}