package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최적_경로_조회를_요청(Long source, Long target, String type) {
        return 두_역의_최적_경로_조회를_요청(source, target, type, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 두_역의_최적_경로_조회를_요청(Long source, Long target, String type, RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
