package co.kr.apti.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Kouydong
 * @apiNote 맴버(클라이언트) 삭제 시 오류 처리
 */
@Setter
@Getter
@AllArgsConstructor
public class ClientDeleteException extends RuntimeException {

    private int code;
    private String codeName;
    private HttpStatus httpStatus;

}
