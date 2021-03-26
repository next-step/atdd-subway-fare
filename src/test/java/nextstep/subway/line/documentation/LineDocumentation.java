package nextstep.subway.line.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("지하철 노선 등록")
    void addLine() {
        LineResponse lineResponse = new LineResponse(
            1L,
            "신분당선",
            "red",
            Arrays.asList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "광교역", LocalDateTime.now(), LocalDateTime.now())
            ),
            900,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(lineService.saveLine(any(LineRequest.class))).thenReturn(lineResponse);


        지하철_노선_생성_요청(
            RestAssured.given(spec).log().all().filter(
                document(
                    "line",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                        fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("기점역 ID"),
                        fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("종점역 ID"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
                        fieldWithPath("additionalFare").type(JsonFieldType.NUMBER).description("추가 요금")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("노선 ID"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                        fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("노선에 포함된 역들"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 아이디"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                        fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("생성일"),
                        fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("수정일"),
                        fieldWithPath("additionalFare").type(JsonFieldType.NUMBER).description("추가 요금"),
                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일"),
                        fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("수정일")
                    )

                )
            ),
            new LineRequest(
                lineResponse.getName(),
                lineResponse.getColor(),
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()).getId(),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now()).getId(),
                10,
                10,
                900
            )
        );

    }
}
