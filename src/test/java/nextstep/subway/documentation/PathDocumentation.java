package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.response.PathResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.support.PathSteps.경로_조회에_성공한다;
import static nextstep.subway.acceptance.support.PathSteps.지하철_경로_조회_요청;
import static nextstep.subway.fixture.SectionFixture.강남_역삼_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.역삼역;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 문서화")
class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 지하철_경로 {

        @Nested
        @DisplayName("지하철 경로 조회 요청을 성공하면")
        class Context_with_find_path {

            @BeforeEach
            void setUp() {
                PathResponse pathResponse = new PathResponse(
                        Lists.newArrayList(
                                강남역.응답_데이터_생성(1L),
                                역삼역.응답_데이터_생성(2L)
                        ),
                        강남_역삼_구간.구간_거리()
                );

                when(pathService.findPath(1L, 2L))
                        .thenReturn(pathResponse);
            }

            @Test
            @DisplayName("200 응답 코드로 응답한다")
            void it_responses_200() throws Exception {
                ExtractableResponse<Response> 지하철_경로_조회_결과 = 지하철_경로_조회_요청(spec, 1L, 2L);

                경로_조회에_성공한다(지하철_경로_조회_결과);
            }
        }
    }
}
