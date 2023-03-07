package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentation extends Documentation {

    private static final String CREATE_LINE = "createLine";
    private static final String GET_LINES = "getLines";
    private static final String GET_LINE = "getLine";
    private static final String UPDATE_LINE = "updateLine";
    private static final String DELETE_LINE = "deleteLine";
    @MockBean
    private LineService lineService;

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "green", Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
        ));
        when(lineService.saveLine(any())).thenReturn(lineResponse);

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(CREATE_LINE,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행종점역 ID"),
                                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행종점역 ID"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("노선 길이")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("노선 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름")
                        )));
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(requestSpec, 지하철_노선_생성_파라미터("2호선", "green", 1L, 2L, 10));

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.jsonPath().getLong("id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
            assertThat(response.jsonPath().getString("color")).isEqualTo("green");
            assertThat(response.jsonPath().getLong("stations[0].id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("stations[0].name")).isEqualTo("강남역");
            assertThat(response.jsonPath().getLong("stations[1].id")).isEqualTo(2L);
            assertThat(response.jsonPath().getString("stations[1].name")).isEqualTo("역삼역");
        });
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        final List<LineResponse> lineResponses = List.of(
                new LineResponse(1L, "2호선", "green", Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                )),
                new LineResponse(2L, "신분당선", "red", Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(3L, "양재역")
                )));
        when(lineService.findLineResponses()).thenReturn(lineResponses);

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(GET_LINES,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("노선 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("[].color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("[].stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                                fieldWithPath("[].stations[].name").type(JsonFieldType.STRING).description("역 이름")
                        )));
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(requestSpec);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getLong("[0].id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("[0].name")).isEqualTo("2호선");
            assertThat(response.jsonPath().getString("[0].color")).isEqualTo("green");
            assertThat(response.jsonPath().getLong("[0].stations[0].id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("[0].stations[0].name")).isEqualTo("강남역");
            assertThat(response.jsonPath().getLong("[0].stations[1].id")).isEqualTo(2L);
            assertThat(response.jsonPath().getString("[0].stations[1].name")).isEqualTo("역삼역");
            assertThat(response.jsonPath().getLong("[1].stations[0].id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("[1].stations[0].name")).isEqualTo("강남역");
            assertThat(response.jsonPath().getLong("[1].stations[1].id")).isEqualTo(3L);
            assertThat(response.jsonPath().getString("[1].stations[1].name")).isEqualTo("양재역");
        });
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "green", Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
        ));
        when(lineService.findLineResponseById(anyLong())).thenReturn(lineResponse);

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(GET_LINE,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("노선 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름")
                        )));
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(requestSpec, 1L);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getLong("id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
            assertThat(response.jsonPath().getString("color")).isEqualTo("green");
            assertThat(response.jsonPath().getLong("stations[0].id")).isEqualTo(1L);
            assertThat(response.jsonPath().getString("stations[0].name")).isEqualTo("강남역");
            assertThat(response.jsonPath().getLong("stations[1].id")).isEqualTo(2L);
            assertThat(response.jsonPath().getString("stations[1].name")).isEqualTo("역삼역");
        });
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        doNothing().when(lineService).updateLine(anyLong(), any());

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(UPDATE_LINE,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔")
                        )));
        final ExtractableResponse<Response> response = 지하철_노선_수정_요청(requestSpec, 1L, 지하철_노선_수정_파라미터("신분당선", "red"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        doNothing().when(lineService).deleteLine(anyLong());

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(DELETE_LINE,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        final ExtractableResponse<Response> response = 지하철_노선_삭제_요청(requestSpec, 1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, Object> 지하철_노선_생성_파라미터(final String name, final String color, final long upStationId, final long downStationId, final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private Map<String, String> 지하철_노선_수정_파라미터(final String name, final String color) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }
}
