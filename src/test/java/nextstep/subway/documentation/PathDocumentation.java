package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("최단 거리 경로 조회 API")
    @Test
    void findPathByDistance() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                6,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        경로_조회(1L, 2L, "DISTANCE");
    }

    @DisplayName("최소 시간 경로 조회 API")
    @Test
    void findPathByDuration() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                4,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        경로_조회(1L, 2L, "DURATION");
    }

    private void 경로_조회(Long source, Long target, String type) {
        RestAssured
                .given(spec).log().all()
                .filter(document(
                        "path-" + type,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 ID"),
                                parameterWithName("target").description("도착역 ID"),
                                parameterWithName("type").description("경로 타입 (DISTANCE, DURATION)")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("경로 내 역 리스트"),
                                fieldWithPath("stations[].id").description("경로 내 역 ID"),
                                fieldWithPath("stations[].name").description("경로 내 역 이름"),
                                fieldWithPath("distance").description("경로의 총 거리"),
                                fieldWithPath("duration").description("경로의 총 소요시간"),
                                fieldWithPath("fare").description("출발-도착역간 최단거리 기준 요금")
                        )
                    )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
