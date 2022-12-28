package co.kr.apti.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Kouydong
 * @apiNote 파라미터 예외 발생을 처리하는 예외 클래스
 */
@Getter
@AllArgsConstructor
public class ParameterNotValidException extends RuntimeException {
    private int code;
    private String codeName;
    private String filedId;
    private String filedMessage;
    private HttpStatus httpStatus;
}
