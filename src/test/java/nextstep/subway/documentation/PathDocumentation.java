package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.request.RequestParametersSnippet;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RequestParametersSnippet requestParametersSnippet = requestParameters(
            parameterWithName("source").description("출발역"),
            parameterWithName("target").description("도착역"));
        ExtractableResponse<Response> response = 경로_조회_요청(spec, requestParametersSnippet,
            1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
