package org.hilingual.domain.token;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories(basePackages = "org.hilingual.domain.security")
public interface TokenRepository extends CrudRepository<Token, Long> {
}
