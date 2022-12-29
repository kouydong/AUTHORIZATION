package co.kr.apti.authorization;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.config.TokenProvider;
import co.kr.apti.authorization.domain.entity.Member;
import co.kr.apti.authorization.domain.repository.MemberRepository;
import co.kr.apti.authorization.web.controller.MemberController;
import co.kr.apti.authorization.web.dto.response.TokenResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class AuthorizationApplication {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	private final Logger log = (Logger) LoggerFactory.getLogger(MemberController.class);

	// JWT Secret 키
	private Key key;

	// jwt 키 시크릿
	@Value("${token.jwt.secret}")
	private String jwtSecret;

	@Value("${token.access-token.seconds}")
	private String accessTokenSeconds;

	// Refresh Token 초
	@Value("${token.refresh-token.seconds}")
	private String refreshTokenSeconds;


	@PostConstruct
	public void initData() {

		makeUser("ADMIN", "1234", "ROLE_ADMIN");
		makeUser("MANAGER", "1234", "ROLE_MANGER");
		makeUser("USER", "1234", "ROLE_USER");

	}
	// 초기 관리자 토큰 생성
	public void makeUser(String memberId, String password, String roles) {


		String encryptedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);

		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		key = Keys.hmacShaKeyFor(keyBytes);

		// AES 암호화 키(40 bytes)
		String encryptedKey = encryptedPassword.substring(encryptedPassword.length() - 40);

		long now = (new Date()).getTime();
		Date accessTokenExpired = new Date(now + Long.parseLong(accessTokenSeconds) * 1000);

		String accessToken = Jwts.builder()
				// Header 설정
				.setHeaderParam("typ", "jwt")
				// PayLoad 설정
				.setIssuer("API-I") // 토큰발행자(iss)
				.setSubject("APT-I Access Token")   // 클레임제목
				.claim("role", roles)   // 역할(권한)
				.setId(memberId)    // 아이디(jti)
				.setAudience(memberId) // 고객
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
				.setId(memberId)    // 아이디(jti)
				.setAudience(memberId) // 고객
				.setIssuedAt(new Date(now)) // 토큰발행시간(iat)
				.setNotBefore(new Date(now)) // 토큰활성시간(nbf)
				.setExpiration(refreshTokenExpired) // 토큰만기시간정보(exp)
				// Signature 설정
				.signWith(key, SignatureAlgorithm.HS256) // 암호화알고리즘
				.compact();

		memberRepository
				.save(
						Member.builder()
								.memberId(memberId)
								.password(encryptedPassword)
								.refreshToken(refreshToken)
								.encryptedKey(encryptedKey)
								.roles(Arrays.asList(roles))
								.build()
				);
		log.info("{} accessToken {}", roles, accessToken);
		log.info("{} refreshToken {}", roles, refreshToken);
	}


	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

}
