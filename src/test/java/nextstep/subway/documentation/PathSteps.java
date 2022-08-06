package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestSteps;
import org.springframework.http.MediaType;

import static nextstep.subway.documentation.Documentation.PATH_GIVEN_SPEC설정_filter설정;

public class PathSteps extends AcceptanceTestSteps {

    public static ExtractableResponse<Response> Path_경로조회_요청_Source_to_Target(Long sourceStationId, Long targetStationId){
        return PATH_GIVEN_SPEC설정_filter설정()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
