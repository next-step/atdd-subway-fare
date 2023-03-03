package nextstep.subway.documentation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @BeforeEach
    void setup() {
        PathResponse pathResponse = new PathResponse(List.of(
            new StationResponse(1L, "교대역"),
            new StationResponse(2L, "남부터미널역")
        ), 10, 5, 10);

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);
    }

    @Test
    void 최단_거리_경로_탐색() {
        setSpecParameters("DISTANCE");
        ExtractableResponse<Response> response = PathSteps.두_역의_최단_경로_조회를_요청(spec, 1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 최단_시간_경로_탐색() {
        setSpecParameters("DURATION");
        ExtractableResponse<Response> response = PathSteps.두_역의_최단_경로_조회를_요청(spec, 1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void setSpecParameters(String type) {
        spec.queryParam("type", type)
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("경로 시작점의 역 id"),
                    parameterWithName("target").description("경로 도착점의 역 id"),
                    parameterWithName("type").description("최단 경로 구하는 기준 타입")
                ),
                responseFields(
                    fieldWithPath("stations").type(JsonFieldType.ARRAY).description("지하철역"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역의 id"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역의 이름"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("역 사이의 거리"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
                    fieldWithPath("fare").type(JsonFieldType.NUMBER).description("운임요금")
                )
            ));
    }
}
