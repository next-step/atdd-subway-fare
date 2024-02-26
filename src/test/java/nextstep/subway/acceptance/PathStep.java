package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathSearchType;

public class PathStep {
    public static ExtractableResponse<Response> 지하철_경로_조회(Long source, Long target, PathSearchType type) {
        return RestAssured
            .given().log().all()
            .when().get("/paths?source={source}&target={target}&type={type}", source, target, type)
            .then().log().all().extract();
    }
}
