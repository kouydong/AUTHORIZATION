package co.kr.apti.authorization.web.dto.request;

import lombok.Getter;
import lombok.ToString;


public class ApiRequestDto {

    //─────────────────────────────────────────────────────────────────────────────────────────────
    //  입주민 인증 API
    //─────────────────────────────────────────────────────────────────────────────────────────────
    @Getter
    public static class authorization {

        private String aptCode;
        private String dong;
        private String ho;
        private String hp;

    }


}
