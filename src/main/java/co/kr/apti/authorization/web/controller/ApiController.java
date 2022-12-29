package co.kr.apti.authorization.web.controller;

import ch.qos.logback.classic.Logger;
import co.kr.apti.authorization.exception.ParameterNotValidException;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

import static co.kr.apti.authorization.constant.ErrorCode.PARAMETER_NOT_VALID_EXCEPTION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final Logger log = (Logger) LoggerFactory.getLogger(ApiController.class);

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    @Value("${api.apt-i.baseUrl}")
    private String baseUrl;

    /**
     * 관리비 조회(API 호출 방식)
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
     * 입주민 인증(DB 직접 연결 방식)
     * 작성자 : 고의동
     * @param requestDto 요청 파라미터
     */
    @PostMapping("/apartments/users/authorization")
    public ResponseEntity<?> isAuthorization(@Validated @RequestBody ApiRequestDto.authorization requestDto, BindingResult bindingResult) throws Exception {

        String aptCode  = requestDto.getAptCode();
        String dong     = StringUtil.padLeft(requestDto.getDong(), 4, '0');
        String ho       = StringUtil.padLeft(requestDto.getHo(), 4, '0');
        String mobileNo = requestDto.getMobileNo();
        String name     = requestDto.getName();

        log.info("입주민 인증 파라미터 확인");
        log.info("requestDto {} ", requestDto);

        // 파라미터 유효성 검사
        checkValidParameters(bindingResult);

        // 입주민 인증
        Connection connection = dataSource.getConnection();
        DatabaseMetaData dbMeta = connection.getMetaData();
        log.info("DB URL : " + dbMeta.getURL());
        log.info("DB Username : " +dbMeta.getUserName());

        String SQL = "SELECT " +
                "                CASE WHEN count(*) >= 1 THEN 'Y' " +
                "                ELSE 'N' " +
                "                END AS AUTHENTICATED " +
                "        FROM " +
                "            ( " +
                "            SELECT " +
                "                    CASE WHEN OCCU_DATE != '00000000' THEN OCCU_DATE WHEN REG_DATE != '00000000' THEN REG_DATE ELSE UPD_DATE END AS OCCU_DATE, " +
                "                    NVL(REG_DATE,'00000000') AS REG_DATE, " +
                "                    NVL(UPD_DATE,'00000000') AS UPD_DATE, " +
                "                    A.ORGAPTCD, B.APTCD, A.DONGHO, " +
                "                    CASE WHEN INSTR(A.REL,'세대') > 0 THEN 'Y' ELSE 'N' END AS REL " +
                "              FROM MAP.RESIDENT A, HJIN.APT B " +
                "             WHERE A.ORGAPTCD   = B.ORGAPTCD " +
                "               AND B.APTCD      = ? AND A.DONGHO = ? " +
                "               AND A.NAME       = ? " +
                "               AND (A.MOBILE_TEL_NO1 = ? OR A.OFFICE_TEL_NO1 = ? OR A.OFFICE_TEL_NO2 = ? OR A.SMS_TEL_NO1 = ? OR A.SMS_TEL_NO2 = ? ) " +
                "               AND A.DELFLAG IS NULL " +
                "             ORDER BY A.OCCU_DATE DESC, A.MODIFYDT DESC " +
                "            ) TMP";
        jdbcTemplate.setDataSource(dataSource);
        String isAuthenticated = jdbcTemplate.queryForObject(SQL, new Object[]{aptCode,dong+ho, name, mobileNo, mobileNo, mobileNo, mobileNo, mobileNo}, String.class );
        Map resultMap = new HashMap();
        resultMap.put("isAuthenticated", isAuthenticated);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);

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
            String fieldMessage = bindingResult.getFieldError().getDefaultMessage();

            throw new ParameterNotValidException(PARAMETER_NOT_VALID_EXCEPTION.getCode(), PARAMETER_NOT_VALID_EXCEPTION.getCodeName(), filedId, fieldMessage, HttpStatus.BAD_REQUEST);

        }
    }

}
