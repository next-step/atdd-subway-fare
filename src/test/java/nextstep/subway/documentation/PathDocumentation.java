package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static nextstep.subway.documentation.PathSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("경로 관리(문서화)")
public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("경로 찾기 요청")
    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "이호선", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(1L, "이호선", LocalDateTime.now(), LocalDateTime.now())
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        ExtractableResponse<Response> 최단_경로_요청 = 최단_경로_요청(spec, 1L, 2L);

        assertThat(최단_경로_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
