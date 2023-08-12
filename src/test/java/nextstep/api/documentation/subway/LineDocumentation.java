package nextstep.api.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_단일조회_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_생성_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_수정_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_전체조회_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_제거_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.line.LineService;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class LineDocumentation extends Documentation {
    @MockBean
    private LineService lineService;

    private final String name = "신분당선";
    private final String color = "bg-red-600";
    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final LineResponse response = new LineResponse(
            1L, name, color, List.of(
            new StationResponse(upStationId, "강남역"),
            new StationResponse(downStationId, "삼성역")
    ));

    @Test
    void createLine() {
        when(lineService.saveLine(any())).thenReturn(response);

        지하철노선_생성_요청(new LineCreateRequest(name, color, upStationId, downStationId, 10, 10), makeRequestSpec(
                document("line-create",
                        requestFields(
                                fieldWithPath("name").description("노선 이름"),
                                fieldWithPath("color").description("노선 색상"),
                                fieldWithPath("upStationId").description("상행 종점역 id"),
                                fieldWithPath("downStationId").description("하행 종점역 id"),
                                fieldWithPath("distance").description("노선 총 거리"),
                                fieldWithPath("duration").description("노선 총 소요시간")
                        ),
                        responseFields(
                                fieldWithPath("id").description("노선 id"),
                                fieldWithPath("name").description("노선 이름"),
                                fieldWithPath("color").description("노선 색상"),
                                fieldWithPath("stations[].id").description("역 id"),
                                fieldWithPath("stations[].name").description("역 이름")
                        )
                )
        ));
    }

    @Test
    void updateLine() {
        doNothing().when(lineService).updateLine(anyLong(), any());

        지하철노선_수정_요청(1L, new LineUpdateRequest(name, color), makeRequestSpec(
                document("line-update",
                        pathParameters(
                                parameterWithName("id").description("노선 id")
                        ),
                        requestFields(
                                fieldWithPath("name").description("노선 이름"),
                                fieldWithPath("color").description("노선 색상")
                        )
                )
        ));
    }

    @Test
    void deleteLine() {
        doNothing().when(lineService).deleteLine(anyLong());

        지하철노선_제거_요청(1L, makeRequestSpec(
                document("line-delete",
                        pathParameters(
                                parameterWithName("id").description("노선 id")
                        )
                )
        ));
    }

    @Test
    void showLines() {
        when(lineService.findAllLines()).thenReturn(List.of(response));

        지하철노선_전체조회_요청(makeRequestSpec(
                document("line-show",
                        responseFields(
                                fieldWithPath("[].id").description("노선 id"),
                                fieldWithPath("[].name").description("노선 이름"),
                                fieldWithPath("[].color").description("노선 색상"),
                                fieldWithPath("[].stations[].id").description("역 id"),
                                fieldWithPath("[].stations[].name").description("역 이름")
                        )
                )
        ));
    }

    @Test
    void findLine() {
        when(lineService.findLine(anyLong())).thenReturn(response);

        지하철노선_단일조회_요청(1L, makeRequestSpec(
                document("line-find",
                        pathParameters(
                                parameterWithName("id").description("노선 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("노선 id"),
                                fieldWithPath("name").description("노선 이름"),
                                fieldWithPath("color").description("노선 색상"),
                                fieldWithPath("stations[].id").description("역 id"),
                                fieldWithPath("stations[].name").description("역 이름")
                        )
                )
        ));
    }
}
