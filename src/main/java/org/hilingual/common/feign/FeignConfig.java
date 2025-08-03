package org.hilingual.common.feign;

import org.hilingual.Main;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = Main.class)
@Configuration
public class FeignConfig {
}