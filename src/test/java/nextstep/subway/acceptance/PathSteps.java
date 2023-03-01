package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final Long source, final Long target) {
        final RequestSpecification spec = new RequestSpecBuilder().build();
        return 두_역의_최단_거리_경로_조회를_요청(spec, source, target, PathType.DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(final Long source, final Long target) {
        final RequestSpecification spec = new RequestSpecBuilder().build();
        return 두_역의_최단_거리_경로_조회를_요청(spec, source, target, PathType.DURATION);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final RequestSpecification spec, final Long source, final Long target, PathType pathType) {
        return RestAssured
                .given(spec)
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .queryParam("type", pathType)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_추가_요금_추가_요청(final Long lineId, final int extraFare) {
        Map<String, String> param = new HashMap<>();
        param.put("lineId", lineId + "");
        param.put("extraFare", extraFare + "");

        return RestAssured
                .given()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(param)
                .when()
                    .post("/paths")
                .then()
                    .log().all()
                .extract();
    }

}
