package nextstep.subway.documentation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathSearchType;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        PathResponse pathResponse =new PathResponse(
            List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ),
            10,
            20,
            1_250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(), anyInt())).thenReturn(pathResponse);

        // when
        ExtractableResponse<Response> response = PathSteps.지하철_경로_조회(spec, 1L, 2L, PathSearchType.DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
