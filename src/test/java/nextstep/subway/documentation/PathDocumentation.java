package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static nextstep.subway.documentation.PathDocumentationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @DisplayName("경로 조회 문서화")
    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 5
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_또는_시간_경로_조회를_요청(spec, 1L, 2L, PathType.DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
