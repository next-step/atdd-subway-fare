package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.support.CommonSupporter.등록에_성공한다;
import static nextstep.subway.acceptance.support.CommonSupporter.삭제에_성공한다;
import static nextstep.subway.acceptance.support.CommonSupporter.조회에_성공한다;
import static nextstep.subway.acceptance.support.LineSteps.노선_목록_정보가_조회된다;
import static nextstep.subway.acceptance.support.LineSteps.노선_정보가_조회된다;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_목록_조회_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_삭제_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_수정_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.acceptance.support.StationSteps.지하철역_생성_요청;
import static nextstep.subway.fixture.FieldFixture.노선_색깔;
import static nextstep.subway.fixture.LineFixture.사호선;
import static nextstep.subway.fixture.LineFixture.신분당선;
import static nextstep.subway.fixture.LineFixture.이호선;
import static nextstep.subway.fixture.SectionFixture.강남_양재_구간;
import static nextstep.subway.fixture.SectionFixture.교대_강남_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.교대역;
import static nextstep.subway.fixture.StationFixture.양재역;
import static nextstep.subway.utils.JsonPathUtil.문자열로_추출;
import static nextstep.subway.utils.JsonPathUtil.식별자_ID_추출;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private Long 교대역_ID;
    private Long 강남역_ID;
    private Long 양재역_ID;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역_ID = 식별자_ID_추출(지하철역_생성_요청(교대역));
        강남역_ID = 식별자_ID_추출(지하철역_생성_요청(강남역));
        양재역_ID = 식별자_ID_추출(지하철역_생성_요청(양재역));
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> 노선_생성_결과 = 지하철_노선_생성_요청(이호선.등록_요청_데이터_생성(교대역_ID, 강남역_ID, 교대_강남_구간));

        // then
        등록에_성공한다(노선_생성_결과);
        노선_목록_정보가_조회된다(지하철_노선_목록_조회_요청(), 이호선.노선_이름());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청(이호선.등록_요청_데이터_생성(교대역_ID, 강남역_ID, 교대_강남_구간));
        지하철_노선_생성_요청(신분당선.등록_요청_데이터_생성(강남역_ID, 양재역_ID, 강남_양재_구간));

        // when
        ExtractableResponse<Response> 노선_목록_조회_결과 = 지하철_노선_목록_조회_요청();

        // then
        조회에_성공한다(노선_목록_조회_결과);
        노선_목록_정보가_조회된다(지하철_노선_목록_조회_요청(), 이호선.노선_이름(), 신분당선.노선_이름());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> 노선_생성_결과 = 지하철_노선_생성_요청(이호선.등록_요청_데이터_생성(교대역_ID, 강남역_ID, 교대_강남_구간));

        // when
        ExtractableResponse<Response> 노선_조회_결과 = 지하철_노선_조회_요청(노선_생성_결과);

        // then
        조회에_성공한다(노선_조회_결과);
        노선_정보가_조회된다(노선_조회_결과, 이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> 노선_생성_결과 = 지하철_노선_생성_요청(이호선.등록_요청_데이터_생성(교대역_ID, 강남역_ID, 교대_강남_구간));

        // when
        Map<String, String> params = new HashMap<>();
        params.put(노선_색깔.필드명(), 사호선.노선_색깔());
        지하철_노선_수정_요청(노선_생성_결과, params);

        // then
        ExtractableResponse<Response> 노선_조회_결과 = 지하철_노선_조회_요청(노선_생성_결과);
        조회에_성공한다(노선_조회_결과);
        노선_색깔이_변경된다(노선_조회_결과, 사호선.노선_색깔());
    }

    private void 노선_색깔이_변경된다(ExtractableResponse<Response> 노선_조회_결과, String 변경된_노선_색깔) {
        assertThat(문자열로_추출(노선_조회_결과, 노선_색깔)).isEqualTo(변경된_노선_색깔);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 노선_생성_결과 = 지하철_노선_생성_요청(이호선.등록_요청_데이터_생성(교대역_ID, 강남역_ID, 교대_강남_구간));

        // when
        ExtractableResponse<Response> 노선_삭제_결과 = 지하철_노선_삭제_요청(노선_생성_결과);

        // then
        삭제에_성공한다(노선_삭제_결과);
    }
}
