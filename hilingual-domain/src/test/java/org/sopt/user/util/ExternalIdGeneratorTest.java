package org.sopt.user.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExternalIdGeneratorTest {

    // ---------- 성공 케이스 ----------

    @Test
    @DisplayName("provider와 providerId로 SHA-256 hex를 생성한다 (SQL 백필값과 동일해야 함)")
    void generates_sha256_hex() {
        // given
        String provider = "GOOGLE";
        String providerId = "12345";

        // when
        String externalId = ExternalIdGenerator.generate(provider, providerId);

        // then
        // 반드시 postgres `encode(digest('GOOGLE:12345','sha256'),'hex')` 결과와 일치해야 한다
        assertThat(externalId)
                .isEqualTo("dd06ae66ba46f4c6ab26ee61bd5be0b9f369b05410142928a3cedd9e81ef07bd");
    }

    @Test
    @DisplayName("생성된 값은 항상 64자 소문자 hex 형식이다")
    void result_is_64_char_lowercase_hex() {
        // given
        String provider = "APPLE";
        String providerId = "001234.abcdef";

        // when
        String externalId = ExternalIdGenerator.generate(provider, providerId);

        // then
        assertThat(externalId).hasSize(64);
        assertThat(externalId).matches("[0-9a-f]{64}"); // 소문자 hex만
    }

    @Test
    @DisplayName("같은 입력이면 항상 같은 값을 반환한다 (결정적)")
    void same_input_returns_same_value() {
        // given
        String provider = "GOOGLE";
        String providerId = "same-id";

        // when
        String first = ExternalIdGenerator.generate(provider, providerId);
        String second = ExternalIdGenerator.generate(provider, providerId);

        // then
        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("providerId가 다르면 다른 값을 반환한다")
    void different_providerId_returns_different_value() {
        // given
        String provider = "GOOGLE";

        // when
        String a = ExternalIdGenerator.generate(provider, "id-1");
        String b = ExternalIdGenerator.generate(provider, "id-2");

        // then
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("구분자(:) 덕분에 조합 경계가 다르면 다른 값을 반환한다")
    void delimiter_prevents_concatenation_ambiguity() {
        // given
        // 구분자가 없다면 "AB"+"C" 와 "A"+"BC" 가 같은 "ABC"로 충돌할 수 있다

        // when
        String ab_c = ExternalIdGenerator.generate("AB", "C");   // "AB:C"
        String a_bc = ExternalIdGenerator.generate("A", "BC");   // "A:BC"

        // then
        assertThat(ab_c).isNotEqualTo(a_bc);
    }

    // ---------- 실패 케이스 ----------

    @Test
    @DisplayName("provider가 null이면 IllegalArgumentException이 발생한다")
    void throws_when_provider_is_null() {
        // when & then
        assertThatThrownBy(() -> ExternalIdGenerator.generate(null, "id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("providerId가 null이면 IllegalArgumentException이 발생한다")
    void throws_when_providerId_is_null() {
        // when & then
        assertThatThrownBy(() -> ExternalIdGenerator.generate("GOOGLE", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }
}
