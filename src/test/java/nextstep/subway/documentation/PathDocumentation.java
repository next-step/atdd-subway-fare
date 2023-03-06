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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
        PathResponse pathResponse = PathResponse.of(
                List.of(new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")),
                // TODO: 기능 구현 완료 후 요금 수정
                10, 3, 1250);
        when(pathService.findPath(any())).thenReturn(pathResponse);

        // when
        createDocumentationRequest("pathDistance", sourceId, targetId, ShortestPathType.DISTANCE);
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
        PathResponse pathResponse = PathResponse.of(
                List.of(new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")),
                // TODO: 기능 구현 완료 후 요금 수정
                10, 3, 1250);
        when(pathService.findPath(any())).thenReturn(pathResponse);

        // when
        createDocumentationRequest("pathTime", sourceId, targetId, ShortestPathType.TIME);
        var response = 타입별_최단_경로_조회_요청(spec);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2)
        );
    }

    private void createDocumentationRequest(String identifier, Long sourceId, Long targetId, ShortestPathType type) {
        spec.queryParams("source", sourceId, "target", targetId, "type", type)
                .filter(document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("Departure station information."),
                                parameterWithName("target").description("Destination station information."),
                                parameterWithName("type").description("Shortest path type."))
                ));
    }
}
