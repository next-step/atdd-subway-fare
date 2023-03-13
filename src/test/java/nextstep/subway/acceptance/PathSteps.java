package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(final RequestSpecification requestSpecification, final long source, final long target, final String findType) {
        return requestSpecification
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", findType)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
