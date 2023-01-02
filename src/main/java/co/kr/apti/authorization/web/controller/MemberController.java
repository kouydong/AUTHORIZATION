package co.kr.apti.authorization.web.controller;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.domain.entity.Member;
import co.kr.apti.authorization.exception.EntityDuplicatedException;
import co.kr.apti.authorization.exception.ParameterNotValidException;
import co.kr.apti.authorization.service.MemberService;
import co.kr.apti.authorization.web.dto.request.MemberRequestDto;
import co.kr.apti.authorization.web.dto.response.MemberJoinResponseDto;
import co.kr.apti.authorization.web.dto.response.TokenResponseDto;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static co.kr.apti.authorization.constant.ErrorCode.ENTITY_DUPLICATED_EXCEPTION;
import static co.kr.apti.authorization.constant.ErrorCode.PARAMETER_NOT_VALID_EXCEPTION;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
public class MemberController {

    private final Logger log = (Logger) LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;




    /**
     * 사용자 정보를 등록.
     * 해당 프로세스는 아파트 아이에서 처리한다.
     * 작성자 : 고의동
     * @param requestDto MemberJoinRequestDto
     */
    @PostMapping("")
    public ResponseEntity<?> joinMember(@Validated @RequestBody MemberRequestDto requestDto, BindingResult bindingResult) {

        // 파라미터 유효성 검사
        checkValidParameters(bindingResult);

        log.info("requestDto {}", requestDto);

        String memberId = requestDto.getMemberId();
        // 패스워드를 만들기 위해 스프링 시큐리티에서 제공하는 대표 인코딩 방식 사용
        String encryptedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(requestDto.getPassword());

        // AES 암호화 키(40 bytes)
        String encryptedKey = encryptedPassword.substring(encryptedPassword.length() - 40);

        // 맴버존재유무확인
        Optional<Member> member = memberService.findByMember(memberId);

        log.info("해당 값은 ==>" + member.isEmpty());
        // 맴버가 없다면 신규 맴버 등록
        if(member.isEmpty()) {
            Member newMember = memberService.joinMember(memberId, encryptedPassword, encryptedKey);
        // 맴버가 존재한다면 해당 정보 출력
        } else {
            encryptedPassword = member.get().getPassword();
            encryptedKey = member.get().getEncryptedKey();
        }









        return ResponseEntity.status(HttpStatus.CREATED).body(
                MemberJoinResponseDto
                        .builder()
                        .memberId(memberId)
                        .encryptedPassword(encryptedPassword)
                        .encryptedKey(encryptedKey)
                        .build()
                );
    }


    /**
     * 사용자 정보를 삭제.
     * 해당 프로세스는 아파트 아이에서 처리한다.
     * 작성자 : 고의동
     * @param requestDto MemberJoinRequestDto
     */
    @DeleteMapping("")
    public ResponseEntity<?> deleteMember(@Validated @RequestBody MemberRequestDto requestDto, BindingResult bindingResult) {

        // 파라미터 유효성 검사
        checkValidParameters(bindingResult);

        log.info("requestDto {} ", requestDto);
        String memberId = requestDto.getMemberId();
        String password = requestDto.getPassword();

        memberService.deleteMember(memberId, password);

        return ResponseEntity.noContent().build();
    }

    /**
     * 사용자 ID와 암호화된 패스워드를 통해서 Access 및 Refresh Token 생성 및 데이터 베이스 저장
     * Generating the access and refresh token with using the user ID and password which is encrypted.
     *
     * 프로세스는 클라이언트에서 처리한다.
     * This process is in charged of the clients.
     *
     * 작성자 : 고의동
     * author : kouydong
     *
     * @param requestDto MemberLoginRequestDto
     */
    @PostMapping("/tokens/access")
    public ResponseEntity<?> memberLogin(@Validated @RequestBody MemberRequestDto requestDto, BindingResult bindingResult) {

        // 파라미터 유효성 검사
        checkValidParameters(bindingResult);

        log.info("MemberRequestDto {}", requestDto);
        String memberId = requestDto.getMemberId();
        String password = requestDto.getPassword();

        TokenResponseDto tokenResponseDto = memberService.memberLogin(memberId, password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", tokenResponseDto.getAccessToken());
        httpHeaders.set("RefreshToken" , tokenResponseDto.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(new GsonBuilder().setPrettyPrinting().create().toJson(tokenResponseDto));

    }


    /**
     * Access Token 만기 전 갱신 처리를 진행
     * Updating the access token
     *
     * 작성자 : 고의동
     * author : kouydong
     *
     * @param requestDto MemberLoginRequestDto
     */
    @PostMapping("/tokens/refresh")
    public ResponseEntity<?> updateAccessToken(@RequestHeader String refreshToken, @Validated @RequestBody MemberRequestDto requestDto, BindingResult bindingResult ) {

        // 파라미터 유효성 검사
        checkValidParameters(bindingResult);

        log.info("MemberRequestDto {}", requestDto);
        requestDto.setRefreshToken(refreshToken);

        // Refresh Token 유효성 검증 검증 실패 시 TokenException 발생
        memberService.isAuthenticated(refreshToken);

        // 토큰 발급
        String memberId = requestDto.getMemberId();
        String password = requestDto.getPassword();

        TokenResponseDto tokenResponseDto = memberService.memberLogin(memberId, password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", tokenResponseDto.getAccessToken());
        httpHeaders.set("RefreshToken" , tokenResponseDto.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(new GsonBuilder().setPrettyPrinting().create().toJson(tokenResponseDto));
    }


    /**
     * @author kouydong
     * @apiNote 요청 파리미터의 유효성을 검사함
     * @return void
     * @throws ParameterNotValidException 유효성 위배시 발생
     */
    public void checkValidParameters(BindingResult bindingResult) throws ParameterNotValidException {

        if(bindingResult.hasErrors()) {

            String filedId = bindingResult.getFieldError().getField();
            String filedMessage = bindingResult.getFieldError().getDefaultMessage();

            throw new ParameterNotValidException(PARAMETER_NOT_VALID_EXCEPTION.getCode(), PARAMETER_NOT_VALID_EXCEPTION.getCodeName(), filedId, filedMessage, HttpStatus.BAD_REQUEST);

        }
    }
}
