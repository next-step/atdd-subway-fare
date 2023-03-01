package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 로그인_후_요청_기준으로_경로_조회_요청(Long sourceId, Long targetId, String shortType, String accessToken) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, shortType)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청(Long sourceId, Long targetId, String shortType) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, shortType)
                .then().log().all().extract();
    }

}
