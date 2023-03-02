package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    private Long sourceId = 1L;
    private Long targetId = 2L;

    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")),
                10);

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // when
        var response = 경로_조회_요청(spec, sourceId, targetId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
