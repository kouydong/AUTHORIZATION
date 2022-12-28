package co.kr.apti.authorization.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class MemberRequestDto
{
    @NotBlank(message = "맴버(클라이언트)아이디없음")
    private String memberId;

    @NotBlank(message = "맴버(클라이언트)패스워드없음")
    private String password;

    private String refreshToken;
}
