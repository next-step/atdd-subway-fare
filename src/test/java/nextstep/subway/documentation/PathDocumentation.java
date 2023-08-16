package nextstep.subway.documentation;

import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {

        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ),
            16,
            180,
                1450
        );

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        PathSteps.두_역의_경로_조회를_요청_docs(1L, 2L, PathType.DISTANCE.name(),
                getSpec("path", getPathRequestParamSnippet(), getPathResponseFieldsSnippet())
        );
    }

    private RequestParametersSnippet getPathRequestParamSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역 id"),
                parameterWithName("target").description("도착역 id"),
                parameterWithName("type").description("탐색 타입 [DISTANCE(거리), DURATION(시간)]")
        );
    }

    private ResponseFieldsSnippet getPathResponseFieldsSnippet() {
        return responseFields(
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 역 정보"),
            fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 id"),
            fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
            fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
            fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 소유시간"),
            fieldWithPath("fare").type(JsonFieldType.NUMBER).description("운임요금")
        );
    }
}
