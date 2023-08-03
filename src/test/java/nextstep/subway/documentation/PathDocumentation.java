package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.RequestPartFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

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
            10,
            180
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        PathSteps.두_역의_최단_거리_경로_조회를_요청_docs(1L, 2L, PathType.DISTANCE.name(), getSpec("path"));
    }

    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
            fieldWithPath("eventTime").type(JsonFieldType.STRING).description("이벤트 시간"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태값"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
            fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 id값")
        );
    }
}
