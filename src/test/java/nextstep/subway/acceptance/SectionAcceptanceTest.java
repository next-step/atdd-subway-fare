package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.support.CommonSupporter.잘못된_요청으로_인해_요청에_실패한다;
import static nextstep.subway.acceptance.support.CommonSupporter.조회에_성공한다;
import static nextstep.subway.acceptance.support.LineSteps.역이_순서대로_정렬되어_있다;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.support.LineSteps.지하철_노선에_지하철_구간_제거_요청;
import static nextstep.subway.acceptance.support.StationSteps.지하철역_생성_요청;
import static nextstep.subway.fixture.LineFixture.신분당선;
import static nextstep.subway.fixture.SectionFixture.강남_양재_구간;
import static nextstep.subway.fixture.SectionFixture.강남_정자_구간;
import static nextstep.subway.fixture.SectionFixture.양재_정자_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.양재역;
import static nextstep.subway.fixture.StationFixture.정자역;
import static nextstep.subway.utils.JsonPathUtil.식별자_ID_추출;

@DisplayName("지하철 구간 관리 기능 인수 테스트")
class SectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선_ID;
    private Long 강남역_ID;
    private Long 양재역_ID;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역_ID = 식별자_ID_추출(지하철역_생성_요청(강남역));
        양재역_ID = 식별자_ID_추출(지하철역_생성_요청(양재역));
        신분당선_ID = 식별자_ID_추출(지하철_노선_생성_요청(신분당선, 강남역_ID, 양재역_ID, 강남_양재_구간.구간_거리()));
    }

    /**
     * When 지하철 노선에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        // when
        Long 정자역_ID = 식별자_ID_추출(지하철역_생성_요청(정자역));
        지하철_노선에_지하철_구간_생성_요청(신분당선_ID, 양재_정자_구간, 양재역_ID, 정자역_ID);

        // then
        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_조회_요청(신분당선_ID);
        조회에_성공한다(지하철_노선_조회_결과);
        역이_순서대로_정렬되어_있다(지하철_노선_조회_결과, 강남역_ID, 양재역_ID, 정자역_ID);
    }

    /**
     * When 지하철 노선 가운데에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선 가운데에 구간을 추가")
    @Test
    void addLineSectionMiddle() {
        // when
        Long 정자역_ID = 식별자_ID_추출(지하철역_생성_요청(정자역));
        지하철_노선에_지하철_구간_생성_요청(신분당선_ID, 강남_정자_구간, 강남역_ID, 정자역_ID);

        // then
        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_조회_요청(신분당선_ID);
        조회에_성공한다(지하철_노선_조회_결과);
        역이_순서대로_정렬되어_있다(지하철_노선_조회_결과, 강남역_ID, 정자역_ID, 양재역_ID);
    }

    /**
     * When 지하철 노선에 이미 존재하는 구간 추가를 요청 하면
     * Then 노선에 새로운 구간추가를 실패한다
     */
    @DisplayName("이미 존재하는 구간을 추가")
    @Test
    void addSectionAlreadyIncluded() {
        // when
        ExtractableResponse<Response> 지하철_구간_생성_결과 = 지하철_노선에_지하철_구간_생성_요청(신분당선_ID, 강남_양재_구간, 강남역_ID, 양재역_ID);

        // then
        잘못된_요청으로_인해_요청에_실패한다(지하철_구간_생성_결과);
    }

    /**
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 지하철 노선의 마지막 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다
     */
    @DisplayName("지하철 노선의 마지막 구간을 제거")
    @Test
    void removeLineSection() {
        // given
        Long 정자역_ID = 식별자_ID_추출(지하철역_생성_요청(정자역));
        지하철_노선에_지하철_구간_생성_요청(신분당선_ID, 양재_정자_구간, 양재역_ID, 정자역_ID);

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선_ID, 정자역_ID);

        // then
        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_조회_요청(신분당선_ID);
        조회에_성공한다(지하철_노선_조회_결과);
        역이_순서대로_정렬되어_있다(지하철_노선_조회_결과, 강남역_ID, 양재역_ID);
    }

    /**
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 지하철 노선의 가운데 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다
     */
    @DisplayName("지하철 노선의 가운데 구간을 제거")
    @Test
    void removeLineSectionInMiddle() {
        // given
        Long 정자역_ID = 식별자_ID_추출(지하철역_생성_요청(정자역));
        지하철_노선에_지하철_구간_생성_요청(신분당선_ID, 양재_정자_구간, 양재역_ID, 정자역_ID);

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선_ID, 양재역_ID);

        // then
        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_조회_요청(신분당선_ID);
        조회에_성공한다(지하철_노선_조회_결과);
        역이_순서대로_정렬되어_있다(지하철_노선_조회_결과, 강남역_ID, 정자역_ID);
    }
}
