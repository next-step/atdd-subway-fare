package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.domain.PathType;

public class PathUtils {

    public static ExtractableResponse<Response> 지하철역_경로조회(Long sourceStationId,
        Long targetStationId, PathType pathType) {
        return RestAssured.given().log().all()
            .when()
            .queryParam("source", sourceStationId)
            .queryParam("target", targetStationId)
            .queryParam("type", pathType)
            .get("/paths")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인후_지하철역_경로조회(Long sourceStationId,
        Long targetStationId, PathType pathType, String accessToken) {

        return RestAssured.given().log().all()
            .when()
            .header("Authorization", "Bearer " + accessToken)
            .queryParam("source", sourceStationId)
            .queryParam("target", targetStationId)
            .queryParam("type", pathType)
            .get("/paths")
            .then().log().all()
            .extract();
    }
}
