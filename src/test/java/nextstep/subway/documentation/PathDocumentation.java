package nextstep.subway.documentation;

import static nextstep.subway.acceptance.document.PathDocumentSteps.두_역의_최단_경로_조회를_요청;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentRequest;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathFindType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10, 10, 1250
        );

        when(pathService.findPath(1L, 2L, PathFindType.DISTANCE)).thenReturn(pathResponse);

        var document = document("path",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description("Bearer JWT 토큰값").optional()
            ),
            requestParameters(
                parameterWithName("source").description("출발하는 역 id"),
                parameterWithName("target").description("도착하는 역 id"),
                parameterWithName("type").description("경로 조회 방법: DURATION: 최단시간, DISTANCE: 최단거리")
            ),
            responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로에 포함된 역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 소요 시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("총 요금")
            ));

        두_역의_최단_경로_조회를_요청(1L, 2L, PathFindType.DISTANCE, null, this.spec, document);
    }
}
