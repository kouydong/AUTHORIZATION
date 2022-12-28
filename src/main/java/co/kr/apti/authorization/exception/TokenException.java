package co.kr.apti.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Kouydong
 * @apiNote 중복 예외 사항을 처리하는 예외 클래스
 */
@Setter
@Getter
@AllArgsConstructor
public class TokenException extends RuntimeException {

    private int code;
    private String codeName;
    private String message;
    private HttpStatus httpStatus;

}
