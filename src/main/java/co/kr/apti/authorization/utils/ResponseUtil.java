package co.kr.apti.authorization.utils;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.web.dto.response.ExceptionTokenResponseDto;
import com.google.gson.GsonBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    private static final Logger log = (Logger) LoggerFactory.getLogger(ResponseUtil.class);

    /**
     * 스프링 시큐리티
     * 작성자 : 고의동
     * @param response HttpServletResponse 객체
     * @param code 아파트아이인증서버에서사용하는 에러코드
     * @param codeName 아파트아이인증서버에서사용하는 에러코드명
     * @param message 예외클래스가던지는메시지
     * @param httpStatus http상태코드(403 Forbidden이 발생하도록 처리)
     */
    public static void setErrorResponse(HttpServletResponse response, int code, String codeName, String message, HttpStatus httpStatus)
    {
        try
        {
            log.error("인증 예외 발생 : " + message);

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(httpStatus.value());
            response.getWriter().write(
                    new GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                            .toJson(
                                    ExceptionTokenResponseDto
                                            .builder()
                                            .code(code)
                                            .codeName(codeName)
                                            .message(message)
                                            .build()
                            )
            );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
