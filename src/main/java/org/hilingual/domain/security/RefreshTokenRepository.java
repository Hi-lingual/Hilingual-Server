package org.hilingual.domain.security;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories(basePackages = "org.hilingual.domain.security")
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
