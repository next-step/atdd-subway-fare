package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.SectionFixture;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.fixture.FieldFixture.노선_내_역_아이디;
import static nextstep.subway.fixture.FieldFixture.노선_색깔;
import static nextstep.subway.fixture.FieldFixture.노선_이름;
import static nextstep.subway.fixture.FieldFixture.데이터_생성_결과_로케이션;
import static nextstep.subway.utils.JsonPathUtil.List로_추출;
import static nextstep.subway.utils.JsonPathUtil.문자열로_추출;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineFixture 노선, SectionFixture 구간, Long 상행역, Long 하행역) {
        Map<String, String> 생성_요청_데이터 = 노선.등록_요청_데이터_생성(상행역, 하행역, 구간);

        return 지하철_노선_생성_요청(생성_요청_데이터);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String accessToken, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> 노선_생성_결과) {
        return RestAssured
                .given().log().all()
                .when().get(노선_생성_결과.header(데이터_생성_결과_로케이션.필드명()))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> 노선_생성_결과, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(노선_생성_결과.header(데이터_생성_결과_로케이션.필드명()))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> 노선_생성_결과) {
        return RestAssured
                .given().log().all()
                .when().delete(노선_생성_결과.header(데이터_생성_결과_로케이션.필드명()))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long 노선_ID, SectionFixture 구간, Long 상행역, Long 하행역) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(구간.요청_데이터_생성(상행역, 하행역))
                .when().post("/lines/{lineId}/sections", 노선_ID)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(String accessToken, Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }


    public static void 역이_순서대로_정렬되어_있다(ExtractableResponse<Response> 지하철_노선_조회_결과, Long... 역_순서) {
        assertThat(List로_추출(지하철_노선_조회_결과, 노선_내_역_아이디, Long.class)).containsExactly(역_순서);
    }

    public static void 노선_목록_정보가_조회된다(ExtractableResponse<Response> 노선_목록_조회_결과, String... 노선명) {
        assertThat(문자열로_추출(노선_목록_조회_결과, 노선_이름)).contains(노선명);
    }

    public static void 노선_정보가_조회된다(ExtractableResponse<Response> 노선_조회_결과, LineFixture 노선) {
        assertThat(문자열로_추출(노선_조회_결과, 노선_이름)).isEqualTo(노선.노선_이름());
        assertThat(문자열로_추출(노선_조회_결과, 노선_색깔)).isEqualTo(노선.노선_색깔());
    }
}
