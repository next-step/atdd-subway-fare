package nextstep.subway.documentation;

import io.restassured.RestAssured;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 10, 1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        var response = Path_경로조회_요청_최소시간(1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getInt("distance")).isEqualTo(10);
    }

    public ExtractableResponse<Response> Path_경로조회_요청_최소시간(Long sourceStationId, Long targetStationId) {
        return PATH_GIVEN_SPEC설정_filter설정()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .queryParam("type", "DURATION")
                .when()
                .get("/paths")
                .then()
                .log()
                .all()
                .extract();
    }

    public RequestSpecification PATH_GIVEN_SPEC설정_filter설정() {
        return RestAssured
                .given(spec)
                .log()
                .all()
                .filter(document("path",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                findPathRequestParameters(),
                                findPathResponseParameters()
                        )
                );
    }

    private ResponseFieldsSnippet findPathResponseParameters() {
        return responseFields(
                fieldWithPath("stations[]").description("경로에 포함된 역의 목록"),
                fieldWithPath("stations[].id").description("Id"),
                fieldWithPath("stations[].name").description("이름"),
                fieldWithPath("distance").description("경로 거리"),
                fieldWithPath("duration").description("경로 소요 시간"),
                fieldWithPath("fare").description("경로 비용")
        );
    }

    private RequestParametersSnippet findPathRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("Source station id"),
                parameterWithName("target").description("Target station id"),
                parameterWithName("type").description("최소 거리로 조회할건지, 최소 시간으로 조회할건지의 조건")
        );
    }
}
