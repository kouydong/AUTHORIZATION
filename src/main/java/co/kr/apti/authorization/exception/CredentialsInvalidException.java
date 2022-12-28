package co.kr.apti.authorization.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Kouydong
 * @apiNote API 호출 실패 시 예외사항을 처리하는 예외 클래스
 */
@Setter
@Getter
public class CredentialsInvalidException extends RuntimeException {
    private int code;
    private String codeName;
    private HttpStatus httpStatus;
    private String errorMessage;


    /**
     * @author Kouydong
     * @apiNote 생성자
     * @param code 응답코드
     * @param codeName 응답메시지
     */
    public CredentialsInvalidException(int code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }


    /**
     * @author Kouydong
     * @apiNote 생성자
     * @param code 응답코드
     * @param codeName 응답메시지
     * @param errorMessage 에러상세메시지
     */
    public CredentialsInvalidException(int code, String codeName, String errorMessage) {
        this.code = code;
        this.codeName = codeName;
        this.errorMessage   = errorMessage;
    }


    /**
     * @author Kouydong
     * @apiNote 생성자
     * @param code 응답코드
     * @param codeName 응답메시지
     * @param httpStatus HTTP 상태코드
     */
    public CredentialsInvalidException(int code, String codeName, HttpStatus httpStatus) {
        this.code = code;
        this.codeName = codeName;
        this.httpStatus     = httpStatus;
    }
}
