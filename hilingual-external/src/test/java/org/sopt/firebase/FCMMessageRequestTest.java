package org.sopt.firebase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sopt.firebase.dto.FCMMessageRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FCMMessageRequestTest {

    @Test
    @DisplayName("정상 생성 시 모든 필드가 올바르게 설정된다")
    void create_success() {
        FCMMessageRequest request = FCMMessageRequest.of("token123", "제목", "내용");

        assertThat(request.token()).isEqualTo("token123");
        assertThat(request.title()).isEqualTo("제목");
        assertThat(request.body()).isEqualTo("내용");
        assertThat(request.data()).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("token이 null, 빈 문자열, 공백이면 예외 발생")
    void create_blankToken_throws(String token) {
        assertThatThrownBy(() -> FCMMessageRequest.of(token, "제목", "내용"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("token");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("title이 null, 빈 문자열, 공백이면 예외 발생")
    void create_blankTitle_throws(String title) {
        assertThatThrownBy(() -> FCMMessageRequest.of("token123", title, "내용"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("body가 null, 빈 문자열, 공백이면 예외 발생")
    void create_blankBody_throws(String body) {
        assertThatThrownBy(() -> FCMMessageRequest.of("token123", "제목", body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("body");
    }

    @Test
    @DisplayName("data가 null이면 빈 Map으로 초기화된다")
    void create_nullData_becomesEmptyMap() {
        FCMMessageRequest request = new FCMMessageRequest("token123", "제목", "내용", null);

        assertThat(request.data()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("data는 방어적 복사되어 외부 변경의 영향을 받지 않는다")
    void create_data_defensiveCopy() {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");

        FCMMessageRequest request = FCMMessageRequest.of("token123", "제목", "내용", original);
        original.put("newKey", "newValue"); // 원본 변경

        assertThat(request.data()).hasSize(1); // 영향 없음
        assertThat(request.data()).containsEntry("key", "value");
    }
}
