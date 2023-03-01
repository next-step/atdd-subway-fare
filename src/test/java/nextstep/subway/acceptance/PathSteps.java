package nextstep.subway.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {
    public static ExtractableResponse<Response> 지하철_경로_조회(RequestSpecification spec, Long source, Long target) {
        return RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
