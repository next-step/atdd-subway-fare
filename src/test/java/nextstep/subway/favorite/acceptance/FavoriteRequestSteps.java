package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class FavoriteRequestSteps {

    public static ExtractableResponse<Response> 지하철_즐겨찾기_추가_요청(RequestSpecification given, TokenResponse tokenResponse, StationResponse target, StationResponse source) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(target.getId(), source.getId());

        return given
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_즐겨찾기_목록_조회_요청(RequestSpecification given, TokenResponse tokenResponse) {
        return given
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_즐겨찾기_제거_요청(RequestSpecification given, TokenResponse tokenResponse, Long favoriteId) {
        return given
                .auth().oauth2(tokenResponse.getAccessToken())
                .pathParam("favoriteId", favoriteId)
                .when().delete("/favorites/{favoriteId}")
                .then().log().all()
                .extract();
    }
}
