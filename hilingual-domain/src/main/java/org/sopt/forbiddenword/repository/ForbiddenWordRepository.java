package org.sopt.forbiddenword.repository;

import org.sopt.forbiddenword.domain.ForbiddenWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, Long> {
    @Query("""
        SELECT EXISTS (
            SELECT 1 FROM ForbiddenWord f WHERE LOWER(:nickname) LIKE CONCAT('%', LOWER(f.forbiddenWord), '%')
        )
    """)
    boolean existsByForbiddenWordInNickname(@Param("nickname") String nickname);
}