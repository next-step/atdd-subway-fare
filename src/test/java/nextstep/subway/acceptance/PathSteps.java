package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.constant.SearchType;
import org.springframework.http.MediaType;

public class PathSteps extends AcceptanceTestSteps {

    public static ExtractableResponse<Response> searchType에_따른_두_역의_최단_경로_조회를_요청(String accessToken, Long source, Long target, SearchType searchType) {
        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&searchType={searchType}", source, target, searchType.name())
                .then().log().all().extract();
    }
}
