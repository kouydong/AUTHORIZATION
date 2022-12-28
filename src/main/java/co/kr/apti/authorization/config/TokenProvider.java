package co.kr.apti.authorization.config;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.exception.TokenException;
import co.kr.apti.authorization.exception.handler.AuthorizationExceptionHandler;
import co.kr.apti.authorization.web.dto.response.TokenResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static co.kr.apti.authorization.constant.ErrorCode.*;


@Configuration
public class TokenProvider {

    private final Logger log = (Logger) LoggerFactory.getLogger(TokenProvider.class);

    // JWT Secret 키
    private Key key;

    // jwt 키 시크릿
    @Value("${token.jwt.secret}")
    private String jwtSecret;

    // Access Token 초
    @Value("${token.access-token.seconds}")
    private String accessTokenSeconds;

    // Refresh Token 초
    @Value("${token.refresh-token.seconds}")
    private String refreshTokenSeconds;


    //────────────────────────────────────────────────────────────────────────────────
    //  생성자가 제일 먼저 초기화 되므로 @PostConstruct 어노테이션을 통해 우선 순위 설정
    //────────────────────────────────────────────────────────────────────────────────
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 인증된 유저 정보를 가지고 Access 및 Refresh Token 생성
     * @param authentication 인증정보
     * @return 토큰 정보
     */
    public TokenResponseDto generateToken(Authentication authentication) {
        //────────────────────────────────────────────────────────────────────────────────
        // 역할(권한) 가져오기
        //────────────────────────────────────────────────────────────────────────────────
        String authorities =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        // 현재 시간 설정
        long now = (new Date()).getTime();
        //────────────────────────────────────────────────────────────────────────────────
        // Access Token 구조 설정
        //────────────────────────────────────────────────────────────────────────────────
        Date accessTokenExpired = new Date(now + Long.parseLong(accessTokenSeconds) * 1000);

        String accessToken = Jwts.builder()
                // Header 설정
                .setHeaderParam("typ", "jwt")
                // PayLoad 설정
                .setIssuer("API-I") // 토큰발행자(iss)
                .setSubject("APT-I Access Token")   // 클레임제목
                .claim("role", authorities)   // 역할(권한)
                .setId(authentication.getName())    // 아이디(jti)
                .setAudience(authentication.getName()) // 고객
                .setIssuedAt(new Date(now)) // 토큰발행시간(iat)
                .setNotBefore(new Date(now)) // 토큰활성시간(nbf)
                .setExpiration(accessTokenExpired) // 토큰만기시간정보(exp)
                // Signature 설정
                .signWith(key, SignatureAlgorithm.HS256) // 암호화알고리즘
                .compact();


        //────────────────────────────────────────────────────────────────────────────────
        // Refresh Token 구조 설정
        //────────────────────────────────────────────────────────────────────────────────
        Date refreshTokenExpired = new Date(now + Long.parseLong(refreshTokenSeconds) * 1000);

        String refreshToken = Jwts.builder()
                // Header 설정
                .setHeaderParam("typ", "jwt")
                // PayLoad 설정
                .setIssuer("API-I") // 토큰발행자(iss)
                .setSubject("APT-I Refresh Token")   // 클레임제목
                .setId(authentication.getName())    // 아이디(jti)
                .setAudience(authentication.getName()) // 고객
                .setIssuedAt(new Date(now)) // 토큰발행시간(iat)
                .setNotBefore(new Date(now)) // 토큰활성시간(nbf)
                .setExpiration(refreshTokenExpired) // 토큰만기시간정보(exp)
                // Signature 설정
                .signWith(key, SignatureAlgorithm.HS256) // 암호화알고리즘
                .compact();

        return TokenResponseDto.builder()
                .accessToken("Bearer " + accessToken)
                .refreshToken("Bearer " + refreshToken)
                .roles(Arrays.asList(authorities.replaceAll("ROLE_","").split(",")))
                .build();
    }
    //────────────────────────────────────────────────────────────────────────────────
    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    //────────────────────────────────────────────────────────────────────────────────
    public Authentication getAuthentication(String accessToken) {
        //────────────────────────────────────────────────────────────────────────────────
        // 토큰 복호화
        //────────────────────────────────────────────────────────────────────────────────
        Claims claims = parseClaims(accessToken);
        //────────────────────────────────────────────────────────────────────────────────
        // 역할(권한)확인
        //────────────────────────────────────────────────────────────────────────────────
        if (claims.get("role") == null) {
            AuthorizationExceptionHandler handler = new AuthorizationExceptionHandler();
            throw new TokenException(TOKEN_ROLE_EMPTY_EXCEPTION.getCode(), TOKEN_ROLE_EMPTY_EXCEPTION.getCodeName(), TOKEN_ROLE_EMPTY_EXCEPTION.getCodeName(), HttpStatus.FORBIDDEN);
        }
        //────────────────────────────────────────────────────────────────────────────────
        // 클레임에서 권한 정보 가져오기
        //────────────────────────────────────────────────────────────────────────────────
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        //────────────────────────────────────────────────────────────────────────────────
        // UserDetails 객체를 만들어서 Authentication 리턴
        //────────────────────────────────────────────────────────────────────────────────
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("토큰 유효성 예외 발생 : {} ", e.getMessage());
            throw new TokenException(TOKEN_INVALID_EXCEPTION.getCode(), TOKEN_INVALID_EXCEPTION.getCodeName(), e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ExpiredJwtException e) {
            // 엑세스 토큰 만기가 발생한 경우 처리 방안(리프레쉬 토큰을 통해 엑세스 재발급 등)
            log.error("토큰 유효성 예외 발생 : {} ", e.getMessage());
            throw new TokenException(TOKEN_EXPIRED_EXCEPTION.getCode(), TOKEN_EXPIRED_EXCEPTION.getCodeName(), e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UnsupportedJwtException e) {
            log.error("토큰 유효성 예외 발생 : {} ", e.getMessage());
            throw new TokenException(TOKEN_UNSUPPORTED_EXCEPTION.getCode(), TOKEN_UNSUPPORTED_EXCEPTION.getCodeName(), e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            log.error("토큰 유효성 예외 발생 : {} ", e.getMessage());
            throw new TokenException(TOKEN_CLAIM_EMPTY_EXCEPTION.getCode(), TOKEN_CLAIM_EMPTY_EXCEPTION.getCodeName(), e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}