package co.kr.apti.authorization.web.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


public class ApiRequestDto {

    //─────────────────────────────────────────────────────────────────────────────────────────────
    //  입주민 인증 API
    //─────────────────────────────────────────────────────────────────────────────────────────────
    @Getter
    public static class authorization {
        @NotBlank(message = "아파트코드 미존재")
        private String aptCode;
        @NotBlank(message = "아파트동 미존재")
        private String dong;
        @NotBlank(message = "아파트호 미존재")
        private String ho;
        @NotBlank(message = "휴대폰번호 미존재")
        private String mobileNo;
        @NotBlank(message = "인증성명 미존재")
        private String name;

    }


}
