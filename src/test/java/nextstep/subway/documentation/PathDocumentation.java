package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.SearchType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class PathDocumentation extends Documentation {
    public static final long SOURCE_ID = 1L;
    public static final long TARGET_ID = 2L;
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // Given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(SOURCE_ID, "강남역"),
                        new StationResponse(TARGET_ID, "역삼역")
                ), 10, 10, 1_250);
        when(pathService.findPath(anyLong(), anyLong(), any(SearchType.class))).thenReturn(pathResponse);

        RequestSpecification requestSpecification = documentConfig(
                "path",
                List.of(parameterWithName("source").description("출발역 id"),
                        parameterWithName("target").description("도착역 id"),
                        parameterWithName("type").description("경로 조회 조건")),
                List.of(fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로의 지하철 역 목록"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철 역 id"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철 역 이름"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로의 이동거리"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로의 이동시간"),
                        fieldWithPath("fare").type(JsonFieldType.NUMBER).description("지하철 이용요금"))
        );

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(SOURCE_ID, TARGET_ID, SearchType.DURATION.name(), requestSpecification);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
