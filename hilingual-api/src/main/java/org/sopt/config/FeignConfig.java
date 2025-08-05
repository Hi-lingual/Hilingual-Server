package org.sopt.config;


import org.sopt.HilingualApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = HilingualApplication.class)
@Configuration
public class FeignConfig {
}