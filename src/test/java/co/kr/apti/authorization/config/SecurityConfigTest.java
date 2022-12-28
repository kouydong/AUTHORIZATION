package co.kr.apti.authorization.config;

import co.kr.apti.authorization.domain.entity.Member;
import co.kr.apti.authorization.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = "spring.profiles.active=local")
class SecurityConfigTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Admin 토큰을 생성합니다.")
    public void createAdminToken() {

        String memberId = "kudd";
        String password = "1234";
        String encryptedKey = "";

        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        String encryptedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);

        memberRepository
                .save(
                        Member.builder()
                                .memberId(memberId)
                                // 스프링 시큐리티에서 제공하는 대표 패스워드 인코딩 사용
                                .password(encryptedPassword)
                                .encryptedKey(encryptedKey)
                                .roles(roles)
                                .build()
                );
    }

}