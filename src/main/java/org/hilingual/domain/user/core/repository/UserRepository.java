package org.hilingual.domain.user.core.repository;

import org.hilingual.domain.user.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
