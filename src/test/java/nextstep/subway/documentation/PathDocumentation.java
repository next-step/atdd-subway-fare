package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.경로_찾기_문서화;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

@DisplayName("경로 관련 기능 문서")
public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("경로 찾기 기능 문서")
    @Test
    void path() {
        long source = 1L;
        long target = 2L;

        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(source, "강남역"),
                        new StationResponse(target, "역삼역")
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RestDocumentationFilter document = document(
                "path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("source").description("시작역"),
                        parameterWithName("target").description("종착역")
                ), responseFields(
                        fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 역 목록"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 거리")
                )
        );

        경로_찾기_문서화(spec, document, source, target);
    }
}
