package co.kr.apti.authorization.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class TokenResponseDto {

    private String grantType;       // Header Grant Type(Bearer)
    private String accessToken;     // Access Token
    private String refreshToken;    // Refresh token
    private List<String> roles;           // Role

}
