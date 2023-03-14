package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    private static final String FIND_PATH = "path";
    @MockBean
    private PathService pathService;

    @Test
    void findPath() {
        final PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 1250);
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(FIND_PATH,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("경로 조회 타입 (distance | duration)")
                        ),
                        responseFields(
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 거리"),
                                fieldWithPath("fare").type(JsonFieldType.STRING).description("최단 거리 경로 기준 요금 (원)")
                        )));
        final ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(requestSpec, 1L, 2L, "distance");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
