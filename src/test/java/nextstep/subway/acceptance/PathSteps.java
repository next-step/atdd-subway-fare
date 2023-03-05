package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Map;

public class PathSteps {
    public static final String DISTANCE = "DISTANCE";
    public static final String DURATION = "DURATION";

    public static ExtractableResponse<Response> 두_역의_가장_빠른_경로_조회를_요청(Long source, Long target, LocalDateTime departureDate) {
        return 두_역의_최단_경로_조회를_요청(createRequestSpecification(source, target, DURATION, departureDate));
    }

    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_조회를_요청(createRequestSpecification(source, target, DURATION));
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_조회를_요청(createRequestSpecification(source, target, DISTANCE));
    }

    public static ExtractableResponse<Response> 인증_회원_두_역의_최단_시간_경로_조회를_요청(String token, Long source, Long target) {
        return 인증_회원_두_역의_최단_경로_조회를_요청(token, createRequestSpecification(source, target, DURATION));
    }

    public static ExtractableResponse<Response> 인증_회원_두_역의_최단_거리_경로_조회를_요청(String token, Long source, Long target) {
        return 인증_회원_두_역의_최단_경로_조회를_요청(token, createRequestSpecification(source, target, DISTANCE));
    }

    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(RequestSpecification specification) {
        return RestAssured
                .given(specification).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 인증_회원_두_역의_최단_경로_조회를_요청(String token, RequestSpecification specification) {
        return RestAssured
                .given(specification).log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static Map<String, String> createPathParams(Long source, Long target, String type) {
        return createPathParams(source, target, type, LocalDateTime.now());
    }

    private static Map<String, String> createPathParams(Long source, Long target, String type, LocalDateTime departureDate) {
        return Map.of("source", source + "", "target", target + "", "type", type, "departureDate", departureDate + "");
    }

    private static RequestSpecification createRequestSpecification(Long source, Long target, String type) {
        return new RequestSpecBuilder()
                .addQueryParams(createPathParams(source, target, type))
                .build();
    }

    private static RequestSpecification createRequestSpecification(Long source, Long target, String type, LocalDateTime departureDate) {
        return new RequestSpecBuilder()
                .addQueryParams(createPathParams(source, target, type, departureDate))
                .build();
    }
}
