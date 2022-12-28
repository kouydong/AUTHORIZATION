package co.kr.apti.authorization.web.dto.response;

import lombok.Builder;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author kouydong
 * @apiNote 예외 발생 시 응답 객체
 */
@Setter
public class ExceptionResponseDto {

    private int resultCode;
    private String message;
    private HttpStatus httpStatus;
    private String fieldId;
    private String fieldMessage;

}
