package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단단_거리_경로_조회를_요청;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentRequest;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
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
            ), 10
        );

        when(pathService.findPath(1L, 2L)).thenReturn(pathResponse);

        var document = document("path",
            getDocumentRequest(),
            getDocumentResponse(),
            responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로에 포함된 역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리")
            ));

        두_역의_최단단_거리_경로_조회를_요청(1L, 2L, this.spec, document);
    }
}
