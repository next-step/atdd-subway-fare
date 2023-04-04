package nextstep.subway.acceptance.support;

import static nextstep.subway.acceptance.support.MemberSteps.로그인된_상태;
import static nextstep.subway.fixture.FieldFixture.경로_요금;
import static nextstep.subway.fixture.FieldFixture.경로_조회_도착지_아이디;
import static nextstep.subway.fixture.FieldFixture.경로_조회_출발지_아이디;
import static nextstep.subway.fixture.FieldFixture.경로_조회_타입;
import static nextstep.subway.fixture.FieldFixture.구간_거리;
import static nextstep.subway.fixture.FieldFixture.구간_소요시간;
import static nextstep.subway.utils.JsonPathUtil.Integer로_추출;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.fixture.MemberFixture;
import org.springframework.http.MediaType;

public class PathSteps {

    public static Map<String, String> 경로_찾기_요청_데이터_생성(long 출발역_id, long 도착역_id, String 타입) {
        Map<String, String> params = new HashMap<>();
        params.put(경로_조회_출발지_아이디.필드명(), String.valueOf(출발역_id));
        params.put(경로_조회_도착지_아이디.필드명(), String.valueOf(도착역_id));
        params.put(경로_조회_타입.필드명(), 타입);
        return params;
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(long 출발역_id, long 도착역_id, String 타입) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(경로_찾기_요청_데이터_생성(출발역_id, 도착역_id, 타입))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_후_지하철_경로_조회_요청(MemberFixture 인증_주체, long 출발역_id, long 도착역_id, String 타입) {
        return RestAssured
                .given(로그인된_상태(인증_주체)).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(경로_찾기_요청_데이터_생성(출발역_id, 도착역_id, 타입))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 총_거리와_소요_시간이_조회된다(ExtractableResponse<Response> 경로_조회_결과, int 총_구간거리, int 총_소요시간) {
        assertSoftly(softly -> {
            softly.assertThat(Integer로_추출(경로_조회_결과, 구간_거리)).isEqualTo(총_구간거리);
            softly.assertThat(Integer로_추출(경로_조회_결과, 구간_소요시간)).isEqualTo(총_소요시간);
        });
    }

    public static void 총_이용_요금이_조회된다(ExtractableResponse<Response> 경로_조회_결과, int 총_요금) {
        assertThat(Integer로_추출(경로_조회_결과, 경로_요금)).isEqualTo(총_요금);
    }
}
