package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청하는_문서;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
            List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // when
        var response = 두_역의_최단_거리_경로_조회를_요청하는_문서(getPathSpec(), 1L, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private RequestSpecification getPathSpec() {
        return getSpec(
            "path",
            getPathsRequestField(),
            getPathsResponseField()
        );
    }

    private RequestParametersSnippet getPathsRequestField() {
        return requestParameters(
            parameterWithName("source").description("출발역 ID"),
            parameterWithName("target").description("도착역 ID")
        );
    }

    private ResponseFieldsSnippet getPathsResponseField() {
        return responseFields(
            fieldWithPath("stations[].id").description("지하철역 ID"),
            fieldWithPath("stations[].name").description("지하철역 이름"),
            fieldWithPath("distance").description("총 거리")
        );
    }
}
