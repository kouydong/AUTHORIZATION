package co.kr.apti.authorization.config;

import co.kr.apti.authorization.exception.TokenException;
import co.kr.apti.authorization.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static co.kr.apti.authorization.constant.ErrorCode.UNKNOWN_EXCEPTION;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean
{
    private final TokenProvider tokenProvider;

    /**
     * GenericFilterBean 추상 클래스를 상속 받아 메서도를 재정의함
     * @param request  ServletRequest 객체
     * @param response ServletResponse 객체
     * @param chain    진행 사항에 대한 요청 및 응답을 통과하기 위한 필터
     *                 Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    {
        //────────────────────────────────────────────────────────────────────────────────────────────────────
        // Request Header 토큰 추출
        //────────────────────────────────────────────────────────────────────────────────────────────────────
        String token = resolveToken((HttpServletRequest) request);
        try
        {
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            // 토큰 유효성 검사
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            if (token != null && tokenProvider.validateToken(token))
            {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 옴
                Authentication authentication = tokenProvider.getAuthentication(token);
                // 가지고 온 authentication 객체를 Spring security Context root 메모리에 로드함
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }
        catch(TokenException te)
        {
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            //  토큰 검증에서 발생하는 유효성 예외처리
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            ResponseUtil.setErrorResponse((HttpServletResponse) response, te.getCode(), te.getCodeName(), te.getMessage(), te.getHttpStatus());
        }
        catch(Exception e)
        {
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            //  알려지지 않은 예외 처리
            //────────────────────────────────────────────────────────────────────────────────────────────────────
            ResponseUtil.setErrorResponse((HttpServletResponse) response, UNKNOWN_EXCEPTION.getCode(), UNKNOWN_EXCEPTION.getCodeName(),UNKNOWN_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Request Header로 부터 토큰 정보를 추출
     * 작성자 : 고의동
     * @param request HttpServletRequest 객체
     * @return 추출된 Token 정보
     */
    private String resolveToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
        {
            return bearerToken.substring(7);
        }
        return null;
    }
}