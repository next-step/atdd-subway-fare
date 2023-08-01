package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청_docs(Long source, Long target) {
        return 두_역의_최단_거리_경로_조회를_요청_docs(source, target, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청_docs(Long source, Long target,
        RequestSpecification spec) {

        return RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
    }
}
