package nextstep.api.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.subway.path.PathSteps.최단경로조회_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.applicaion.path.dto.PathResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.path.PathSelection;

class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        final var response = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "삼성역")
                ), 10, 10, 1250
        );

        when(pathService.findShortestPath(any(), anyLong(), anyLong(), anyString())).thenReturn(response);

        최단경로조회_요청(1L, 2L, PathSelection.DISTANCE, makeRequestSpec(
                document("path",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).optional()
                                        .description("필수 헤더 X) 사용자 나이별 할인정책 적용을 위함")
                        ),
                        requestParameters(
                                parameterWithName("source").description("출발역 id"),
                                parameterWithName("target").description("도착역 id"),
                                parameterWithName("type").description("경로조회타입")
                        ),
                        responseFields(
                                fieldWithPath("stations[].id").description("역 id"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("distance").description("총 거리"),
                                fieldWithPath("duration").description("총 소요시간"),
                                fieldWithPath("fare").description("이용 요금"))
                )
        ));
    }
}
