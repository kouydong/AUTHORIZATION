package co.kr.apti.authorization.web.controller;

import co.kr.apti.authorization.domain.entity.Member;
import co.kr.apti.authorization.domain.repository.MemberRepository;
import org.apache.logging.log4j.util.Base64Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// 로컬 프로파일로 테스트 진행
@SpringBootTest(properties = "spring.profiles.active=local")
class MemberControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자 등록")
    public void createMember() {
        String memberId = "SAMSUNG";
        String password = "1234";
        memberRepository.save(Member.builder()
                        .memberId(memberId)
                        .password(password)
                        .build());
    }






    /*
    @Test
    @DisplayName("인증 헤더 만들기")
    public void makeAuthorizationHeader() throws Exception {

        //─────────────────────────────────────────────────────────────────────────────────
        //
        //─────────────────────────────────────────────────────────────────────────────────
        String clientId     = "samsung";
        String clientSecret = "sOIoXrB1CopaiAYBm4pY9Ixxo9qfBdvxTEacjsVvW2I=";
        String methodType   = "POST"; // GET/POST/PUT/DELETE
        //─────────────────────────────────────────────────────────────────────────────────
        //  requestUri 만들기
        //─────────────────────────────────────────────────────────────────────────────────
        //  호출할 Uri을 소문자로 변환
        //  java.net.URLEncoder.encode 를 통해 utf-8로 인코딩 변환
        //─────────────────────────────────────────────────────────────────────────────────
        String requestUri   = URLEncoder.encode("https://api.apti.co.kr/api/v2/6052/2/authorization", "utf-8");
        String requestUri2  = URLEncoder.encode("https://api.apti.co.kr/api/v2/6052/2/authorization".toLowerCase(), "utf-8");
        System.out.printf("requestUri %s\n", requestUri);
        System.out.printf("requestUri2 %s\n", requestUri2);
        //─────────────────────────────────────────────────────────────────────────────────
        //  Time stamp 만들기
        //─────────────────────────────────────────────────────────────────────────────────
        //  Timestamp 객체를 이용하는 변환
        //─────────────────────────────────────────────────────────────────────────────────
//        String timeStamp = String.valueOf(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        System.out.println("timestamp ==>" + timestamp);
//        System.out.println("timestamp ==>" + System.currentTimeMillis());


        //─────────────────────────────────────────────────────────────────────────────────
        //  SimpleDateFormat 객체를 이용하는 변환
        //─────────────────────────────────────────────────────────────────────────────────
        Date date = new Date();
        System.out.println("인코딩2-1 값 ==> " + Base64Utils.encodeToString(clientId.getBytes()));
        System.out.println("인코딩2-2 값 ==> " + Base64Util.encode(clientId));
    }
    */



}