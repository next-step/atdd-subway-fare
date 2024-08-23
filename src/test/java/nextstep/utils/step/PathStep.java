package nextstep.utils.step;

import io.restassured.RestAssured;
import nextstep.common.dto.ErrorResponse;
import nextstep.path.dto.PathResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathStep {

    public static PathResponse 경로_조회_거리(Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source=" + source + "&target=" + target + "&type=DISTANCE")
                .then().log().all()
                .extract().response().body().as(PathResponse.class);
    }

    public static PathResponse 경로_조회_거리_헤더_설정(Long source, Long target, String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/paths?source=" + source + "&target=" + target + "&type=DISTANCE")
                .then().log().all()
                .extract().response().body().as(PathResponse.class);
    }

    public static PathResponse 경로_조회_시간(Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source=" + source + "&target=" + target + "&type=DURATION")
                .then().log().all()
                .extract().response().body().as(PathResponse.class);
    }

    public static ErrorResponse 경로_조회_기간_실패(Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source=" + source + "&target=" + target + "&type=DISTANCE")
                .then().log().all()
                .extract().response().body().as(ErrorResponse.class);
    }
}

