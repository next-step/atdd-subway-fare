package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.domain.PathType;
import org.springframework.http.MediaType;

public class PathRequestSteps {

    public static ExtractableResponse<Response> 지하철_최단_거리_및_최소_시간_경로_조회_요청(RequestSpecification given, TokenResponse tokenResponse, Long source, Long target, PathType type) {
        return given
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
