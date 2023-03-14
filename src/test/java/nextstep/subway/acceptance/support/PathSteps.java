package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static nextstep.subway.documentation.Documentation.restDocsFilter;
import static nextstep.subway.fixture.FieldFixture.경로_조회_도착지_아이디;
import static nextstep.subway.fixture.FieldFixture.경로_조회_출발지_아이디;
import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static Map<String, String> 경로_찾기_요청_데이터_생성(Long 출발역_id, Long 도착역_id) {
        Map<String, String> params = new HashMap<>();
        params.put(경로_조회_출발지_아이디.필드명(), String.valueOf(출발역_id));
        params.put(경로_조회_도착지_아이디.필드명(), String.valueOf(도착역_id));
        return params;
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회_요청(long 출발역_id, long 도착역_id) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(경로_찾기_요청_데이터_생성(출발역_id, 도착역_id))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회_요청(RequestSpecification spec, long 출발역_id, long 도착역_id) {
        return given(spec).log().all()
                .filter(restDocsFilter("path"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(경로_찾기_요청_데이터_생성(출발역_id, 도착역_id))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_조회에_성공한다(ExtractableResponse<Response> 지하철_경로_조회_결과) {
        assertThat(지하철_경로_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
