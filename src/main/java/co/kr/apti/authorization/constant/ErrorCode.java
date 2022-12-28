package co.kr.apti.authorization.constant;

import lombok.Getter;


@Getter
public enum ErrorCode {


    // 토큰 예외처리
    TOKEN_INVALID_EXCEPTION(20701, "유효하지 않은 JWT 토큰"),
    TOKEN_EXPIRED_EXCEPTION(20702, "토큰 접속 시간 만기"),
    TOKEN_UNSUPPORTED_EXCEPTION(20703, "지원하지 않는 토큰 정보"),
    TOKEN_CLAIM_EMPTY_EXCEPTION(20704, "토큰 클레임 정보 없음"),
    TOKEN_ROLE_EMPTY_EXCEPTION(20705, "토큰 역할(권한) 정보 없음"),
    REFRESH_INVALID_EXCEPTION(20706, "유효하지 않은 Refresh 토큰"),

    TOKEN_NOT_FOUND_EXCEPTION(20712, "토큰 정보 없음"),


    // 일반 예외처리
    PARAMETER_NOT_VALID_EXCEPTION(1   , "파라미터 유효성 검증 실패"),
    API_CALL_EXCEPTION(9   , "API 호출 실패"),

    API_ENTITY_NOT_FOUND_EXCEPTION(10, "API 호출 결과 없음"),
    CREDENTIALS_INVALID_EXCEPTION(20707, "자격증명 실패"),
    CLIENT_DELETE_EXCEPTION(20708, "맴버(클라이언트) 삭제 실패"),
    ENTITY_NOT_FOUND_EXCEPTION(20710, "클라이언트 미존재"),
    RESOURCE_NOT_ALLOW_EXCEPTION(20711, "Resource 접근 권한 없음"),

    INSERT_FAIL_EXCEPTION(20713, "맴버(클라이언트) 등록 실패"),


    // 상기 사항 까지 사용 확인


    // 아래 사항은 사용 확인 필요
    /** 공통 예외 코드 */



    ENCRYPTION_EXCEPTION(4, "암호화 실패"),
    DECRYPTION_EXCEPTION(5, "복호화 실패"),
    HEADER_NOT_VALID_EXCEPTION(6      , "헤더 인증 오류"),
    FILE_NOT_FOUND_EXCEPTION(7, "파일 찾을 수 없음"),
    FILE_UPLOAD_EXCEPTION(8, "파일 업로드 실패"),



    DUPLICATED_CLIENT_EXCEPTION(20701, "중복된 클라이언트 존재"), // 사용

    UNKNOWN_EXCEPTION(999, "알수없는 예외 오류");




    private final int code;
    private final String codeName;

    /**
     * @author Kouydong
     * @apiNote 생성자
     * @param code 요청코드(int)
     * @param codeName 요청코드명(String)
     * @return ErrorCode
     */
    ErrorCode(int code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }
}

