package co.kr.apti.authorization.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MemberJoinResponseDto
{
    private String memberId; // 사용자 ID
    private String encryptedPassword; // 암호화된 사용자패스워드
    private String encryptedKey; // 파리미터 암호화를 위한 키
}
