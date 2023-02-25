package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void 최단_거리_경로를_탐색한다() {
        경로_응답을_설정한다(new PathResponse(Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")), 10));

        문서화_요청_파라미터를_설정한다(1L, 2L, DISTANCE);
        ExtractableResponse<Response> response = 두_역의_최단_경로_조회를_요청(this.spec);


        경로_응답을_검증한다();
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
    }

    @Test
    void 최단_시간_경로를_탐색한다() {
        경로_응답을_설정한다(new PathResponse(Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")), 10, 5));

        문서화_요청_파라미터를_설정한다(1L, 2L, DURATION);
        ExtractableResponse<Response> response = 두_역의_최단_경로_조회를_요청(this.spec);

        경로_응답을_검증한다();
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
    }

    private void 경로_응답을_설정한다(PathResponse pathResponse) {
        when(pathService.findPath(any())).thenReturn(pathResponse);
    }

    private void 문서화_요청_파라미터를_설정한다(Long source, Long target, String type) {
        this.spec
                .queryParams("source", source, "target", target, "type", type)
                .filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    private void 경로_응답을_검증한다() {
        verify(pathService, times(1)).findPath(any());
    }
}