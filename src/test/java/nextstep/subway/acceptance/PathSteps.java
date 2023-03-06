package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.ShortestPathType;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathSteps {

    public static ExtractableResponse<Response> 타입별_최단_경로_조회_요청(RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 타입별_최단_경로_조회_요청(Long source, Long target, ShortestPathType type) {
        RequestSpecification spec = new RequestSpecBuilder()
                .addQueryParams(Map.of("source", source, "target", target, "type", type))
                .build();

        return 타입별_최단_경로_조회_요청(spec);
    }
}
