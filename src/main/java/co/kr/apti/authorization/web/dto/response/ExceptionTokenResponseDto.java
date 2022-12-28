package co.kr.apti.authorization.web.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * 필터에서 발생하는 토큰 예외 처리를 담당
 * Handling of the Exception in the Filter of Spring Boot Application
 *
 * 작성자 : 고의동
 * author : Kudd
 */
public class ExceptionTokenResponseDto {

    private int code;
    private String codeName;
    private String message;
    private HttpStatus httpStatus;


    @Builder
    public ExceptionTokenResponseDto(int code, String codeName, String message, HttpStatus httpStatus) {
        this.code = code;
        this.codeName = codeName;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
