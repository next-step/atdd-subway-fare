package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.ShortestPathType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.타입별_최단_경로_조회_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    private Long sourceId = 1L;
    private Long targetId = 2L;

    @DisplayName("최단 거리 경로를 조회하고 문서화한다")
    @Test
    void findPath_WithShortestDistancePath() {
        // given
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")),
                10, 3);
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        // when
        createDocumentationRequest(sourceId, targetId, ShortestPathType.DISTANCE);
        var response = 타입별_최단_경로_조회_요청(spec);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2)
        );

    }

    @DisplayName("최단 시간 경로를 조회하고 문서화한다")
    @Test
    void findPath_WithShortestTimePath() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")),
                10, 3);

        // when
        createDocumentationRequest(sourceId, targetId, ShortestPathType.TIME);
        var response = 타입별_최단_경로_조회_요청(spec);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2)
        );
    }

    private void createDocumentationRequest(Long sourceId, Long targetId, ShortestPathType type) {
        spec.queryParam("source", sourceId, "target", targetId, "type", type)
                .filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }
}
