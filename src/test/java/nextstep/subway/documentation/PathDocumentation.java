package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_또는_시간_경로_조회를_요청;
import static nextstep.subway.documentation.DocumentSteps.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @DisplayName("경로 조회 문서화")
    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 5, 1250
        );

        when(pathService.findPath(anyInt(), anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        // when
        RequestSpecification given = given(spec.filter(getDocument("path")));
        ExtractableResponse<Response> response = 두_역의_최단_거리_또는_시간_경로_조회를_요청(given, 1L, 2L, PathType.DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static RestDocumentationFilter getDocument(String identifier) {
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                getRequestParametersSnippet(),
                getResponseFieldsSnippet()
        );
    }

    private static ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("지하철역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 id"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("최단 경로 총 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("최단 경로 총 시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("총 요금")
        );
    }

    private static RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역 id"),
                parameterWithName("target").description("도착역 id"),
                parameterWithName("type").description("최단 경로 기준 : DISTANCE(거리) / DURATION(시간)")
        );
    }
}
