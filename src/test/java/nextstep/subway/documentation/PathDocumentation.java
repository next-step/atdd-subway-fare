package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathLookUpType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                10,
                10
        );
        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        // when
        ExtractableResponse<Response> searchResponse = PathSteps.searchPath(spec, "1", "2", PathLookUpType.DISTANCE.name());

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
