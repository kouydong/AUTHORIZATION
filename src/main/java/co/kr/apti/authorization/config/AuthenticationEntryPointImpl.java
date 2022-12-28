package co.kr.apti.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 스프링 시큐리티 인가 실패 발생 시 예외 핸들러로 AccessDeniedHandler 인터페이스 구현을 통해 예외처리 핸들러를 구현함.
 * 리소스에 권한이 없는 사용자가 접근 시 예외를 발생 시킴(스프링 시큐리티 인가 실패)
 * 해당 어플리케이션에서는 AccessDeniedHandler 인터페이스를 구현을 통한 인가 처리 함
 * 추후 사용 시 프로세스 정의 필요
 * 작성자 : 고의동
 */
@Configuration
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 추후 필요 시 정의 필요.

    }}
