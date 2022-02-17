package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import static nextstep.subway.acceptance.PathSteps.경로_조회;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        Long source = 1L;
        Long target = 2L;
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new Station(source, "가양역"),
                        new Station(target, "역삼역")
                ), 10, 20
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        경로_조회(given("path", this.getParameterDescriptors(), this.getFieldDescriptors()), source, target, "DURATION");
    }

    private ParameterDescriptor[] getParameterDescriptors() {
        return new ParameterDescriptor[]{parameterWithName("source").description("출발역"),
                parameterWithName("target").description("도착역"),
                parameterWithName("type").description("조회 기준")
        };
    }

    private FieldDescriptor[] getFieldDescriptors() {
        return new FieldDescriptor[]{fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("최단 경로 역 리스트"),
                fieldWithPath("stations[].id").description("(최단 경로 역) ID"),
                fieldWithPath("stations[].name").description("(최단 경로 역) 이름"),
                fieldWithPath("distance").description("최단 거리"),
                fieldWithPath("duration").description("최단 시간")
        };
    }
}

