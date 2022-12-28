package co.kr.apti.authorization.config;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * 스프링 시큐리티 설정을 위한 클래스 입니다.
 * @author kouydong
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 인가 실패 핸들러
    private final AuthenticationEntryPoint authenticationEntryPoint;

    // 인가 실패 핸들러
    private final AccessDeniedHandler accessDeniedHandler;

    private final Logger log = (Logger) LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * 이전 버전에서는 WebSecurityConfigurerAdapter 를 상속받아 Overwriting 하여 스프링 시큐리티를 구현 하였으나 최근 버젼에서는 bean 등록하여 사용
     * @param httpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                // 기본적 보안 설정
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                .httpBasic().disable()// http basic 보안을 사용하지 않음
                .csrf().disable()// csrf 보안을 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션을 사용하지 않음
                .and()
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                .headers().frameOptions().disable() // H2 사용 시 iframe 사용 할 수 있도록 조치가 필요하여 해당 내용 제거
                .and()
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                //  스프링 시큐리티 예외 처리
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                .exceptionHandling() // 예외처리 기능 활성화
//                .authenticationEntryPoint(authenticationEntryPoint) // 인가 실패 시 예외 핸들러(accessDeniedHandler 사용)
                .accessDeniedHandler(accessDeniedHandler) // 인가 실패 시 예외 핸들러
                .and()
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                //  ADMIN : 모든 리소스 접근 가능
                //  MANAGER : /member/** 및 /api/** 접근 가능
                //  USER : /api/** 접근 가능
                //  모든 권한들이 엑세스 토큰을 발급 받을 수 있도록 /members/tokens/** 접근 가능
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                .authorizeRequests()// 인증 요청이 들어오면
                .antMatchers("/members/tokens/**","/h2-console/**").permitAll() // 모두 접근 허용
                .antMatchers("/members/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER" ) // MANAGE
                .antMatchers("/api/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")
                .anyRequest().authenticated()
                .and()
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                // 선택적 보안 설정 2
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                // UsernamePasswordAuthenticationFilter 사용하기 전에 JWT 인증을 위해 구현한 JwtAuthenticationFilter 사용
                //────────────────────────────────────────────────────────────────────────────────────────────────────
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * JWT 사용하기 위해서는 기본적으로 password encoder 필요하며 대표적인 패스워드 인코딩 처리
     * @return passwordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}