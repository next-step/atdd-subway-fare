package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathCondition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 5, 1250
        );

        given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        var 최단_경로_조회_결과 = 최단_경로_조회("distance-path", 1L, 2L, PathCondition.DISTANCE);

        경로_조회_완료(최단_경로_조회_결과);
    }

    @Test
    public void find_path_by_time() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 3, 1450
        );

        given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        var 최단_경로_조회_결과 = 최단_경로_조회("duration-path", 1L, 2L, PathCondition.DURATION);

        경로_조회_완료(최단_경로_조회_결과);
    }

    private ExtractableResponse<Response> 최단_경로_조회(String identifier, Long source, Long target, PathCondition distance) {
        return RestAssured
                .given(spec).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document(identifier,
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                findPathRequestParameters(),
                                findPathResponseParameters()
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("pathCondition", distance.name())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private ResponseFieldsSnippet findPathResponseParameters() {
        return responseFields(
                fieldWithPath("stations[].id").description("Id of station"),
                fieldWithPath("stations[].name").description("Name of station"),
                fieldWithPath("distance").description("Distance of path"),
                fieldWithPath("duration").description("Cost time of path"),
                fieldWithPath("fare").description("Fare of path")
        );
    }

    private RequestParametersSnippet findPathRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("Start station id"),
                parameterWithName("target").description("Arrival station id"),
                parameterWithName("pathCondition").description("Search conditions for shortest path")
        );
    }

    private void 경로_조회_완료(ExtractableResponse<Response> 최단_경로_조회_결과) {
        assertThat(최단_경로_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
