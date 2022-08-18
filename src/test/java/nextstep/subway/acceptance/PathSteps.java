package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathSteps extends AcceptanceTestSteps {

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(RequestSpecification given, Long source, Long target, PathType pathType) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(Map.of(
                        "source", source,
                        "target", target,
                        "type", pathType.name()
                ))
                .when().get("/paths")
                .then().log().all().extract();
    }

}
