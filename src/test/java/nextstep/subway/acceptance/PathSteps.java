package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathSearchType;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathSearchType pathSearchType) {
        return 두_역의_경로_조회를_요청(RestAssured.given(), source, target, pathSearchType);
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(RequestSpecification requestSpecification, Long source, Long target, PathSearchType pathSearchType) {
        return requestSpecification.log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("pathSearchType", pathSearchType)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
