package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_또는_시간_경로_조회를_요청(RequestSpecification given, Long source, Long target, PathType pathType) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", pathType.name())
                .when().get("/paths")
                .then().log().all().extract();
    }
}
