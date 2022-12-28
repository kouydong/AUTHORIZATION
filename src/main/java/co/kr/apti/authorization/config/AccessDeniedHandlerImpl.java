package co.kr.apti.authorization.config;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.utils.ResponseUtil;
import co.kr.apti.authorization.web.controller.ApiController;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static co.kr.apti.authorization.constant.ErrorCode.RESOURCE_NOT_ALLOW_EXCEPTION;
/**
 * 스프링 시큐리티 인가 실패 발생 시 예외 핸들러로 AccessDeniedHandler 인터페이스 구현을 통해 예외처리 핸들러를 구현함.
 * 리소스에 권한이 없는 사용자가 접근 시 예외를 발생 시킴(스프링 시큐리티 인가 실패)
 *
 *  작성자 : 고의동
*/
@Configuration
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final Logger log = (Logger) LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

        ResponseUtil.setErrorResponse((HttpServletResponse) response, RESOURCE_NOT_ALLOW_EXCEPTION.getCode(), RESOURCE_NOT_ALLOW_EXCEPTION.getCodeName(),RESOURCE_NOT_ALLOW_EXCEPTION.getCodeName(), HttpStatus.FORBIDDEN);

    }



}
