package co.kr.apti.authorization.service;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.config.TokenProvider;
import co.kr.apti.authorization.domain.entity.Member;
import co.kr.apti.authorization.domain.repository.MemberRepository;
import co.kr.apti.authorization.exception.*;
import co.kr.apti.authorization.web.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.kr.apti.authorization.constant.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final Logger log = (Logger) LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider jwtTokenProvider;


    /**
     * 사용자 존재 유무를 조회해서 있는 경우 예외를 발생 시킴
     * 작성자 : 고의동
     * @param memberId 사용자ID
     */
    public Optional<Member> findByMember(String memberId) {

        return memberRepository.findByMemberId(memberId);

    }

    /**
     * 사용자 정보를 등록한다.
     * 해당 프로세스는 아파트 아이에서 처리한다.
     * 작성자 : 고의동
     * @param memberId 사용자ID
     * @param encryptedPassword 암호화된패스워드
     */
    @Transactional
    public Member joinMember(String memberId, String encryptedPassword, String encryptedKey) {
        //─────────────────────────────────────────────────────────────────────────────────────────────────────────
        //  일반적으로 ADMIN, USER 등으로 구분하나 역할의 경우 Customizing 가능
        //─────────────────────────────────────────────────────────────────────────────────────────────────────────
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        return memberRepository
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


    /**
     * 사용자 정보를 등록한다.
     * 해당 프로세스는 아파트 아이에서 처리한다.
     * 작성자 : 고의동
     */
    @Transactional
    public void deleteMember(String memberId, String password) {

        // 데이터가 없으면 예외 처리
        Optional<Member> member = memberRepository.findByMemberIdAndPassword(memberId, password);
        member.orElseThrow(() -> new ClientDeleteException(ENTITY_NOT_FOUND_EXCEPTION.getCode(), ENTITY_NOT_FOUND_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR));

        // 삭제 처리
        memberRepository.delete(member.get());
    }


    @Transactional
    public TokenResponseDto memberLogin(String memberId, String password) {

        // SpringSecurity UsernamePasswordAuthenticationToken 클래스
        // An org.springframework.security.core.Authentication implementation that is designed for simple presentation of a username and password.
        // The principal and credentials should be set with an Object that provides the respective property via its Object.toString() method. The simplest such Object to use is String.
        // 유저 ID 및 패스워드의 간단한 표현으로 설계된 인증 구현 클래스
        // 주체 및 자격증명은 Object.toString() 통해 각각의 속성이 제공하는 오브젝트로 설정 되어야 함.

        // This constructor can be safely used by any code that wishes to create a UsernamePasswordAuthenticationToken, as the isAuthenticated() will return false.
        // 아래의 UsernamePasswordAuthenticationToken 생성자는 isAuthenticated()가 false 이기 때문에 토큰을 생성함에 있어 어떤 코드도 안정적임.
        // isAuthenticated() false 란 의미는 아직 인증되지 않았고 인증을 위한 객체를 생성함. AuthenticationManager 를 통해 인증 절차를 처리

        // 맴버 아이디와 맴버 롤의 조회
        UsernamePasswordAuthenticationToken authenticationMember = new UsernamePasswordAuthenticationToken(memberId, password);

        // 인증 정보를 가지고 옴
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationMember);

        // 인증 되어 있지 않았다면 예외 발생
        if(!authentication.isAuthenticated()) throw new CredentialsInvalidException(CREDENTIALS_INVALID_EXCEPTION.getCode(), CREDENTIALS_INVALID_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR);
        if(authentication.getAuthorities().isEmpty()) throw new CredentialsInvalidException(ROLE_EMPTY_EXCEPTION.getCode(), ROLE_EMPTY_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);

        Optional<Member> member = memberRepository.findByMemberId(memberId);
        member.orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.getCode(),ENTITY_NOT_FOUND_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR));
        Member updatedMember = member.get();

        // Refresh 토큰이 null 인 경우만 업데이트 함
        if(updatedMember.getRefreshToken() == null) updatedMember.setRefreshToken(tokenResponseDto.getRefreshToken());
        memberRepository.save(updatedMember);

        tokenResponseDto.setRefreshToken(updatedMember.getRefreshToken());
        
        return tokenResponseDto;
    }


    /**
     * Refresh Token을 사용해서 토큰 발급 여부를 확인
     * 작성자 : 고의동
     * @param refreshToken 리프레쉬토큰
     */
    @Transactional
    public boolean isAuthenticated(String refreshToken) {

        Optional<Member> member = memberRepository.isAuthenticated((refreshToken));
        member.orElseThrow(() -> new TokenException(REFRESH_INVALID_EXCEPTION.getCode(), REFRESH_INVALID_EXCEPTION.getCodeName(), REFRESH_INVALID_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR));

        return true;

    }
}

