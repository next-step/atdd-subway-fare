package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성을_요청;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    @Test
    @DisplayName("즐겨찾기를 추가한다.")
    void addFavorite() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("source", 교대역 + "");
        params.put("target", 양재역 + "");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("즐겨찾기를 조회한다.")
    void getFavorites() {
        // given
        즐겨찾기_생성을_요청(token, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", FavoriteResponse.class)).hasSize(1);
    }

    @Test
    @DisplayName("즐겨찾기를 삭제한다.")
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(token, 교대역, 양재역);

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(response.header("Location"))
                .then().log().all().extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}