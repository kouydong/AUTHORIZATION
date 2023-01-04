package co.kr.apti.authorization.exception.handler;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.exception.*;
import co.kr.apti.authorization.web.dto.response.ExceptionResponseDto;
import com.google.gson.GsonBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static co.kr.apti.authorization.constant.ErrorCode.UNKNOWN_EXCEPTION;


/**
 * @author kouydong
 * @apiNote 컨트롤러 예외 사항을 처리하는 예외 핸들러
 */
@RestControllerAdvice
public class AuthorizationExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = (Logger) LoggerFactory.getLogger(AuthorizationExceptionHandler.class);


    /**
     * @author Kouydong
     * @apiNote 사용자 정의 예외 클래스로 파라미터 오류 발생 시 처리
     * @param parameterNotValidException 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(ParameterNotValidException.class)
    public ResponseEntity<?> exceptionHandler(ParameterNotValidException parameterNotValidException) {

        log.warn("ParameterNotValidException 예외 발생 : " + parameterNotValidException.getCodeName());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(parameterNotValidException.getCode());
        responseDto.setMessage(parameterNotValidException.getCodeName());
        responseDto.setFieldId(parameterNotValidException.getFiledId());
        responseDto.setFieldMessage(parameterNotValidException.getFiledMessage());
        responseDto.setHttpStatus(parameterNotValidException.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(parameterNotValidException.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }


    /**
     * @author Kouydong
     * @apiNote API call 실패 시 예외 처리
     * @param apiCallException 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(ApiCallException.class)
    public ResponseEntity<?> exceptionHandler(ApiCallException apiCallException) {

        log.warn("ApiCallException 예외 발생 : " + apiCallException.getCodeName());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(apiCallException.getCode());
        responseDto.setMessage(apiCallException.getCodeName());
        responseDto.setHttpStatus(apiCallException.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(apiCallException.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }


    /**
     * @author Kouydong
     * @apiNote 자격 증명에 실패한 경우
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(CredentialsInvalidException.class)
    public ResponseEntity<?> exceptionHandler(CredentialsInvalidException exception) {

        log.warn("CredentialsFailException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }



    /**
     * @author Kouydong
     * @apiNote 암호화 오류 발생 시 처리
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(EncryptionException.class)
    public ResponseEntity<?> exceptionHandler(EncryptionException exception) {

        log.warn("EncryptionException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }


    /**
     * 맴버(클라이언트) 삭제 예외 처리
     * @author Kouydong
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(ClientDeleteException.class)
    public ResponseEntity<?> exceptionHandler(ClientDeleteException exception) {

        log.warn("ClientDeleteException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }


    /**
     * @author Kouydong
     * @apiNote 복호화 오류 발생 시 처리
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(DecryptionException.class)
    public ResponseEntity<?> exceptionHandler(DecryptionException exception) {

        log.warn("DecryptionException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }


    /**
     * @author Kouydong
     * @apiNote 엔티티를 찾지 못하는 경우 예외 발생
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> exceptionHandler(EntityNotFoundException exception) {

        log.warn("EntityNotFoundException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }

    /**
     * @author Kouydong
     * @apiNote 엔티티 중복이 발생한 경우
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityDuplicatedException.class)
    public ResponseEntity<?> exceptionHandler(EntityDuplicatedException exception) {

        log.warn("EntityDuplicatedException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }



    /**
     * 토큰 예외처리를 진행
     * @author Kouydong
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> exceptionHandler(TokenException exception) {

        log.warn("TokenException 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(exception.getCode());
        responseDto.setMessage(exception.getCodeName());
        responseDto.setHttpStatus(exception.getHttpStatus());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(exception.getHttpStatus()).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }




    /**
     * @author Kouydong
     * @apiNote 예외처리가 되지 않은 경우 예외 발생
     * @param exception 사용자 정의타입 파라미터 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception exception) {

        log.error("Exception 예외 발생 : " + exception.getMessage());

        ExceptionResponseDto responseDto = new ExceptionResponseDto();

        responseDto.setResultCode(UNKNOWN_EXCEPTION.getCode());
        responseDto.setMessage(UNKNOWN_EXCEPTION.getCodeName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return ResponseEntity.status(500).headers(headers).body(new GsonBuilder().setPrettyPrinting().create().toJson(responseDto));
    }
}
