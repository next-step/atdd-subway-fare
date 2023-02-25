package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathRequestType;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 경로_조회(RequestSpecification spec, Long source, Long target, PathRequestType type) {

        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type.name())
                .when().get("/paths")
                .then().log().all().extract();
    }


}
