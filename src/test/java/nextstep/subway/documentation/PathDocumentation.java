package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathCondition;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
                ), 10, 5
        );

        given(pathService.findPath(anyLong(), anyLong())).willReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("path",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("source").description("Start station id"),
                                        parameterWithName("target").description("Arrival station id")
                                ),
                                responseFields(
                                        fieldWithPath("stations[].id").description("Id of station"),
                                        fieldWithPath("stations[].name").description("Name of station"),
                                        fieldWithPath("distance").description("Distance of path"),
                                        fieldWithPath("duration").description("Cost time of path")
                                )
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();
    }

    @Test
    public void find_path_by_time() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 3
        );

        given(pathService.findPath(anyLong(), anyLong())).willReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("path",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("source").description("Start station id"),
                                        parameterWithName("target").description("Arrival station id"),
                                        parameterWithName("pathCondition").description("Search conditions for shortest path")
                                ),
                                responseFields(
                                        fieldWithPath("stations[].id").description("Id of station"),
                                        fieldWithPath("stations[].name").description("Name of station"),
                                        fieldWithPath("distance").description("Distance of path"),
                                        fieldWithPath("duration").description("Cost time of path")
                                )
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("pathCondition", PathCondition.DURATION.name())
                .when().get("/paths")
                .then().log().all().extract();
    }
}
