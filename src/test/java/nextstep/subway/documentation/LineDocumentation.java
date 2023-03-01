package nextstep.subway.documentation;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로찾기 문서화 테스트")
@ExtendWith(MockitoExtension.class)
public class LineDocumentation extends Documentation {
    @MockBean
    private LineService lineService;

    @Test
    void createLine() {
        LineResponse lineResponse = new LineResponse(
                1L,
                "신분당선",
                "red",
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "양재역")),
                1_000);

        BDDMockito.given(lineService.saveLine(any(LineRequest.class))).willReturn(lineResponse);

        LineRequest lineRequest = new LineRequest("신분당선", "red", 1L, 2L, 10, 10, 1_000);

        RestDocumentationFilter filter = document("createLine",
                requestFields(
                        fieldWithPath("name").description("노선 이름"),
                        fieldWithPath("color").description("노선 색상"),
                        fieldWithPath("upStationId").description("상행역 ID"),
                        fieldWithPath("downStationId").description("하행역 ID"),
                        fieldWithPath("distance").description("상행역과 하행역 구간의 거리"),
                        fieldWithPath("duration").description("상행역과 하행역 구간의 소요시간"),
                        fieldWithPath("additionalFare").description("추가 요금")),
                responseFields(
                        fieldWithPath("id").description("노선 ID"),
                        fieldWithPath("name").description("노선 이름"),
                        fieldWithPath("color").description("노선 색상"),
                        fieldWithPath("stations[].id").description("노선에 포함된 지하철역 ID"),
                        fieldWithPath("stations[].name").description("노선에 포함된 지하철역 이름"),
                        fieldWithPath("additionalFare").description("추가 요금")));

        지하철_노선_생성_요청(lineRequest, spec, filter);
    }
}
