package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathRequestType;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 경로_조회(RequestSpecification spec,Long source, Long target, PathRequestType type) {

        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type.name())
                .when().get("/paths")
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 두_역의_거리_경로_조회를_요청(Long source, Long target, PathRequestType type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_후_두_역의_거리_경로_조회를_요청(String accessToken, Long source, Long target, PathRequestType type) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }
}
