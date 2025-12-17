package com.ssafy.foody.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class FindAccountRequest {
	// 아이디 찾기 - 인증번호 요청 (이메일)
    @Data
    public static class FindIdSend {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;
    }

    // 아이디 찾기 - 인증 확인 및 아이디 조회 (이메일 + 인증코드)
    @Data
    public static class FindIdVerify {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "인증번호를 입력해주세요.")
        private String code;
    }

    // 비밀번호 찾기 - 인증번호 발송 요청 (아이디 + 이메일)
    @Data
    public static class FindPwSend {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String id;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;
    }

    // 비밀번호 찾기 - 인증번호 검증 및 임시 비번 발급 (아이디 + 이메일 + 코드)
    @Data
    public static class FindPwVerify {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String id;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        private String email;

        @NotBlank(message = "인증번호를 입력해주세요.")
        private String code;
    }
}
