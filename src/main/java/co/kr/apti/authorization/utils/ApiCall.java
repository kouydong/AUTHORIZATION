package co.kr.apti.authorization.utils;

import co.kr.apti.authorization.exception.ApiCallException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.handler.codec.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static co.kr.apti.authorization.constant.ErrorCode.API_CALL_EXCEPTION;

/**
 * WebFlux 사용하여 API 호출
 * Requesting the API which is deployed other server(e.g. API-SERVER-MAIN) with using the WebFlux
 *
 * 작성자 : 고의동
 * author : Kudd
 */

@Configuration
public class ApiCall {

    private static final Logger log = LoggerFactory.getLogger(ApiCall.class);

    /**
     * Get method API 호출 시 사용
     * Request the API which in method type for only GET
     *
     * 작성자 : 고의동
     * author : kudd
     *
     * @param baseUrl 베이스 URL(Based URL)
     * @param uri URI 경로(URI Path)
     * @return API 호출 결과 값(The result for requesting the specific API)
     *
     * @throws ApiCallException
     */
    public static JsonObject getApiCall(String baseUrl, String uri) throws ApiCallException {

        Mono<String> response = WebClient.create(baseUrl)
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        httpStatus -> !(httpStatus == HttpStatus.OK || httpStatus == HttpStatus.CREATED || httpStatus == HttpStatus.NO_CONTENT),
                        clientResponse -> clientResponse.createException()
                                .flatMap(it -> Mono.error(new ApiCallException(API_CALL_EXCEPTION.getCode(), API_CALL_EXCEPTION.getCodeName(), clientResponse.statusCode())))
                )
                .bodyToMono(String.class)
                .onErrorResume(throwable -> Mono.error(new ApiCallException(API_CALL_EXCEPTION.getCode(), API_CALL_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR)));

        return new Gson().fromJson(response.block(), JsonObject.class);
    }



    /**
     * Post method API 호출 시 사용
     * Request the API which in method type for only POST
     *
     * 작성자 : 고의동
     * author : kudd
     *
     * @param baseUrl 베이스 URL(Based URL)
     * @param uri URI 경로(URI Path)
     * @param reqJson 파라미터(Parameters)
     * @return API 호출 결과 값(The result for requesting the specific API)
     */
    public static JsonObject postApiCall(String baseUrl, String uri, JsonObject reqJson) throws ApiCallException {

        Mono<String> response = WebClient.create(baseUrl)
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reqJson.toString()))
                .retrieve()
                .onStatus(
                        httpStatus -> !(httpStatus == HttpStatus.OK || httpStatus == HttpStatus.CREATED || httpStatus == HttpStatus.NO_CONTENT),
                        clientResponse -> clientResponse.createException()
                                .flatMap(it -> Mono.error(new ApiCallException(API_CALL_EXCEPTION.getCode(), API_CALL_EXCEPTION.getCodeName(), clientResponse.statusCode())))
                )
                .bodyToMono(String.class)
                .onErrorResume(throwable -> Mono.error(new ApiCallException(API_CALL_EXCEPTION.getCode(), API_CALL_EXCEPTION.getCodeName(), HttpStatus.INTERNAL_SERVER_ERROR)));

        return new Gson().fromJson(response.block(), JsonObject.class);
    }

}
