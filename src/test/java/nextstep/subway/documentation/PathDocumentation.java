package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.utils.GenericExtensionUtils.arrayToList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로 테스트(문서)")
public class PathDocumentation extends Documentation {

    private static final String SOURCE = "source";
    private static final String TARGET = "target";

    @MockBean
    private PathService pathService;

    @DisplayName("두 역의 최단 경로를 조회")
    @Test
    void shortestPath() {
        PathResponse pathResponse = createPathResponse(
                10, 15,
                createStationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                createStationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
        );

        when(pathService.findPathByShortestCondition(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path-shortest-distance",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(SOURCE, 1L)
                .queryParam(TARGET, 2L)
                .when().get("/paths")
                .then().log().all().extract();
    }

    @DisplayName("두 역의 최소 시간 경로를 조회")
    @Test
    void shortestTimePath() {
        PathResponse pathResponse = createPathResponse(
                10, 15,
                createStationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                createStationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
        );

        when(pathService.findPathByShortestCondition(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path-shortest-duration",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(SOURCE, 1L)
                .queryParam(TARGET, 2L)
                .when().get("/paths?condition=duration")
                .then().log().all().extract();
    }

    private PathResponse createPathResponse(int distance, int duration, StationResponse... stationResponseArgs) {
        return new PathResponse(arrayToList(stationResponseArgs), distance, duration);
    }

    private StationResponse createStationResponse(Long id, String name, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        return new StationResponse(id, name, createdDateTime, modifiedDateTime);
    }
}
