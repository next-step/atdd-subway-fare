package nextstep.subway.station.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.station.acceptance.documentation.StationDocumentation;
import nextstep.subway.utils.AcceptanceTest;
import nextstep.subway.utils.BaseDocumentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.station.acceptance.StationRequestSteps.*;
import static nextstep.subway.station.acceptance.StationVerificationSteps.*;
import static nextstep.subway.utils.BaseDocumentation.givenDefault;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String DOCUMENT_IDENTIFIER_STATION = "station/{method-name}";

    private BaseDocumentation baseDocumentation;

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        // given
        baseDocumentation = new StationDocumentation(spec);
        RequestSpecification 지하철_역_생성_문서화_요청 = baseDocumentation.requestDocumentOfAllType(DOCUMENT_IDENTIFIER_STATION);

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(지하철_역_생성_문서화_요청, "강남역");

        // then
        지하철_역_생성_됨(response);
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStationWithDuplicateName() {
        //given
        지하철_역_등록_됨(강남역);

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(givenDefault(), 강남역);

        // then
        지하철_역_생성_실패_됨(response);
    }

    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        // given
        baseDocumentation = new StationDocumentation(spec);
        RequestSpecification 지하철_역_목록_조회_문서화_요청 = baseDocumentation.requestDocumentOfDefault(DOCUMENT_IDENTIFIER_STATION);

        ExtractableResponse<Response> createResponse1 = 지하철_역_등록_됨(강남역);
        ExtractableResponse<Response> createResponse2 = 지하철_역_등록_됨(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청(지하철_역_목록_조회_문서화_요청);

        // then
        지하철_역_목록_조회_됨(response);
        지하철_역_목록_조회_결과에_생성한_역_포함_확인(response, Arrays.asList(createResponse1, createResponse2));
    }

    @Test
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        // given
        baseDocumentation = new StationDocumentation(spec);
        RequestSpecification 지하철_역_제거_문서화_요청 = baseDocumentation.requestDocumentOfDefault(DOCUMENT_IDENTIFIER_STATION);

        ExtractableResponse<Response> createResponse = 지하철_역_등록_됨(강남역);

        // when
        ExtractableResponse<Response> response = 지하철_역_제거_요청(지하철_역_제거_문서화_요청, 생성된_지하철_역_URI_경로_확인(createResponse));

        // then
        지하철_역_제거_됨(response);
    }
}
