package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    public static final long SOURCE_ID = 1L;
    public static final long TARGET_ID = 2L;
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // Given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(SOURCE_ID, "강남역"),
                        new StationResponse(TARGET_ID, "역삼역")
                ), 10
        );
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(spec, SOURCE_ID, TARGET_ID);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
