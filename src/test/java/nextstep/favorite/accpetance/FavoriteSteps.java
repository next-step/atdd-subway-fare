package nextstep.favorite.accpetance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.service.dto.FavoritePathRequest;
import nextstep.subway.domain.service.path.StationPathSearchRequestType;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteSteps {
    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_경로_등록(String accessToken, String sourceStationName, String targetStationName,
                                                           StationPathSearchRequestType type,
                                                           Map<String, Long> stationIdByName) {
        return 즐겨찾기_경로_등록(accessToken, sourceStationName, targetStationName, type, stationIdByName, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 즐겨찾기_경로_등록(String accessToken, String sourceStationName, String targetStationName,
                                                           StationPathSearchRequestType type,
                                                           Map<String, Long> stationIdByName, HttpStatus expectedStats) {
        FavoritePathRequest request = new FavoritePathRequest(stationIdByName.get(sourceStationName), stationIdByName.get(targetStationName), type);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .statusCode(expectedStats.value())
                .extract();
    }

    public static void 즐겨찾기_경로_등록됨(String accessToken, String sourceStationName, String targetStationName, int order) {
        var jsonPath = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();

        var source = String.format("[%d].source.name", order);
        var target = String.format("[%d].target.name", order);

        Assertions.assertEquals(sourceStationName, jsonPath.getString(source));
        Assertions.assertEquals(targetStationName, jsonPath.getString(target));
    }

    public static void 즐겨찾기_경로_존재하지않음(String accessToken) {
        var jsonPath = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();

        Assertions.assertEquals(0, jsonPath.getList("").size());
    }

    public static void 즐겨찾기_경로_삭제(String 사용자1_토큰, ExtractableResponse<Response> response) {
        var uri = response.header("Location");

        RestAssured.given().log().all()
                .auth().oauth2(사용자1_토큰)
                .when().delete(uri)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
