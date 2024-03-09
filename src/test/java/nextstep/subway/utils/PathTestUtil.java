package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.entity.PathSearchType;

import java.util.HashMap;
import java.util.Map;

public class PathTestUtil {

    public static ExtractableResponse<Response> 최소_경로_탐색(Long sourceId, Long targetId, PathSearchType type) {
        Map<String, String> params = new HashMap<>();
        params.put("source", sourceId.toString());
        params.put("target", targetId.toString());
        params.put("type", type.toString());

        return RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
