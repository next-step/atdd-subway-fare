package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.response.PathResponse;
import nextstep.subway.applicaion.dto.response.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static nextstep.subway.acceptance.support.CommonSupporter.조회에_성공한다;
import static nextstep.subway.documentation.support.PathDocumentSupport.지하철_경로_조회_요청;
import static nextstep.subway.domain.path.PathType.DISTANCE;
import static nextstep.subway.fixture.SectionFixture.강남_역삼_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.역삼역;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 조회 기능 문서화 테스트")
class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 지하철_경로 {

        @Nested
        @DisplayName("지하철 경로 조회 요청을 성공하면")
        class Context_with_find_path {

            private Long 출발지역_ID = 1L;
            private Long 도착지역_ID = 2L;

            @BeforeEach
            void setUp() {
                ArrayList<StationResponse> 역_목록 = Lists.newArrayList(
                        강남역.응답_데이터_생성(출발지역_ID),
                        역삼역.응답_데이터_생성(도착지역_ID)
                );
                PathResponse pathResponse = new PathResponse(역_목록, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간(), 1250);

                // RestDocs는 테스트 기반인 문서로 신뢰성이 보장되는 장점이 있는데, 테스트의 속도를 위해 Mock 테스트로 진행한다면 신뢰성이 있다고 봐야할까..??
                when(pathService.findPath(출발지역_ID, 도착지역_ID, DISTANCE.name()))
                        .thenReturn(pathResponse);
            }

            @Test
            @DisplayName("200 응답 코드로 응답한다")
            void it_responses_200() {
                ExtractableResponse<Response> 경로_조회_결과 = 지하철_경로_조회_요청(spec, 출발지역_ID, 도착지역_ID, DISTANCE.name());

                조회에_성공한다(경로_조회_결과);
            }
        }
    }
}
