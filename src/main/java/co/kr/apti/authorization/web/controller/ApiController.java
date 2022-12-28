package co.kr.apti.authorization.web.controller;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.utils.ApiCall;
import co.kr.apti.authorization.utils.InternalAes256;
import co.kr.apti.authorization.utils.StringUtil;
import co.kr.apti.authorization.web.dto.request.ApiRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final Logger log = (Logger) LoggerFactory.getLogger(ApiController.class);

    @Value("${api.apt-i.baseUrl}")
    private String baseUrl;

    /**
     * 관리비 조회
     * 작성자 : 고의동
     * @param aptCode 아파트코드(Apartment code)
     * @param dong 아파트동(Apartment dong)
     * @param ho 아파트호(Apartment ho)
     */
    @GetMapping("/apartments/users/{aptCode}/{dong}/{ho}")
    public ResponseEntity<?> findMyManagementFee(@PathVariable String aptCode, @PathVariable String dong, @PathVariable String ho) throws Exception {

        dong    = StringUtil.padLeft(dong, 4, '0');
        ho      = StringUtil.padLeft(ho, 4, '0');

        String dongHo = InternalAes256.encrypt(dong+ho); // 내부 aes256 암호화

        log.debug("aptCode {}", aptCode);
        log.debug("dongHo {}", dongHo);

        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("BillYm", "202201");
        bodyJson.addProperty("FixCd", aptCode);
        bodyJson.addProperty("Dongho", dongHo);

        JsonObject responseJson = ApiCall.postApiCall(baseUrl, "/api/manage/GetManageInfo", bodyJson);

        Map<String, String> responseMap = new ObjectMapper().readValue(responseJson.toString(), Map.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }


    /**
     * 입주민 인증
     * 작성자 : 고의동
     * @param requestDto 요청 파라미터
     */
    @PostMapping("/apartments/users/authorization")
    public ResponseEntity<?>  isAuthorization(@RequestBody ApiRequestDto.authorization requestDto) {

        String aptCode  = requestDto.getAptCode();
        String dong     = StringUtil.padLeft(requestDto.getDong(), 4, '0');
        String ho       = StringUtil.padLeft(requestDto.getHo(), 4, '0');
        String hp       = requestDto.getHp();
        log.info("입주민 인증 파라미터 확인");
        log.info("aptCode {} ", aptCode);
        log.info("dong {} ", dong);
        log.info("ho {} ", ho);
        log.info("hp {} ", hp);

        // API 호출 및 결과값 리턴(예시)
//        JsonObject bodyJson = new JsonObject();
//        bodyJson.addProperty("BillYm", "202201");
//        bodyJson.addProperty("FixCd", aptCode);
//        bodyJson.addProperty("Dongho", dongHo);
//
//        JsonObject responseJson = ApiCall.postApiCall(baseUrl, "/api/manage/GetManageInfo", bodyJson);
//
//        Map<String, String> responseMap = new ObjectMapper().readValue(responseJson.toString(), Map.class);

        return ResponseEntity.status(HttpStatus.OK).body("입주민인증결과값리턴");
    }
}
