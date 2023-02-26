package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathRequestType;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.acceptance.PathSteps.경로_조회;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith(MockitoExtension.class)
class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "교대역"),
                        new StationResponse(2L, "남부터미널역"),
                        new StationResponse(3L, "양재역")
                ), 10, 20, 1450);


        when(pathService.findPath(anyLong(), anyLong(), any(PathRequestType.class))).thenReturn(pathResponse);

        this.spec.filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                setPathRequestParametersDescription(),
                setPathResponseFieldsDescription()
        ));

        ExtractableResponse<Response> response = 경로_조회(spec, 1L, 3L, PathRequestType.DISTANCE);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private RequestParametersSnippet setPathRequestParametersDescription() {
        return requestParameters(
                parameterWithName("source").description("출발역 식별자"),
                parameterWithName("target").description("도착역 식별자"),
                parameterWithName("type").description("조회구분코드")
        );
    }

    private ResponseFieldsSnippet setPathResponseFieldsDescription() {
        return responseFields(
                fieldWithPath("stations").description("역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 식별자"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
        );
    }
}
