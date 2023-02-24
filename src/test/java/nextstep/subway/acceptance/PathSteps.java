package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        RequestSpecification spec = new RequestSpecBuilder()
                .addQueryParams(Map.of("source", source, "target", target, "type", "DURATION"))
                .build();
        return 두_역의_최단_거리_경로_조회를_요청(spec);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        RequestSpecification spec = new RequestSpecBuilder()
                .addQueryParams(Map.of("source", source, "target", target, "type", "DISTANCE"))
                .build();
        return 두_역의_최단_거리_경로_조회를_요청(spec);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification specification) {
        return RestAssured
                .given(specification).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
