package org.hilingual.test;

import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {

    @GetMapping("/success-with-data")
    public ResponseEntity<BaseResponseDto<Map<String, Object>>> successWithData() {
        Map<String, Object> data = Map.of("diaryId", 123);
        return ResponseEntity
                .ok(BaseResponseDto.success(GlobalSuccessCode.OK, data));
    }

    @GetMapping("/success-no-data")
    public ResponseEntity<BaseResponseDto<Void>> successNoData() {
        return ResponseEntity
                .ok(BaseResponseDto.success(GlobalSuccessCode.OK));
    }

    @GetMapping("/ok-default")
    public ResponseEntity<BaseResponseDto<SuccessTestResponse>> successDefault() {
        SuccessTestResponse response = SuccessTestResponse.builder()
                .diaryId(123L)
                .title("오늘의 일기")
                .writer("하륑구")
                .content("정말 신나는 하루였다!")
                .build();
        return ResponseEntity.ok(BaseResponseDto.success(GlobalSuccessCode.OK_CUSTOM, response));
    }

    @GetMapping("/fail")
    public ResponseEntity<BaseResponseDto<Void>> failResponse() {
        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    @GetMapping("/exception")
    public ResponseEntity<BaseResponseDto<Void>> exception() {
        throw new RuntimeException("강제로 발생시킨 예외");
    }

}