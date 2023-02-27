package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")),10);

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // when
        ExtractableResponse<Response> searchResponse = PathSteps.searchPath(spec, 1L, 2L);
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
