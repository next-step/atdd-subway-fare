package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@DisplayName("Subway Path TEST 문서화")
public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Nested
    class 지하철_경로 {

        @Nested
        @DisplayName("지하철 경로 조회 요청을 성공하면")
        class Context_with_find_path {

            @BeforeEach
            void setUp() {
                PathResponse pathResponse = new PathResponse(
                    Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                    ), 10, 10
                );

                when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);
            }

            @Test
            @DisplayName("200 응답 코드로 응답한다")
            void it_responses_200() {
                ExtractableResponse<Response> response = Path_조회_API(spec, 1L, 2L);
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            }
        }
    }
}
