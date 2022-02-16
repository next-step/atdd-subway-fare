package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
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
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now())
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        ExtractableResponse<Response> response = RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("시작역"),
                                parameterWithName("target").description("도착역")),
                        responseFields(
                                fieldWithPath("stations").description("경로에 포함된 역 목록"),
                                fieldWithPath("stations[].id").description("지하철 역 ID"),
                                fieldWithPath("stations[].name").description("지하철 역 이름"),
                                fieldWithPath("stations[].createdDate").description("역 생성일"),
                                fieldWithPath("stations[].modifiedDate").description("역 최근 수정일"),
                                fieldWithPath("distance").description("시작역과 도착역 사이 거리"))))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void pathDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 6
        );

        when(pathService.findPathDuration(anyLong(), anyLong())).thenReturn(pathResponse);

        ExtractableResponse<Response> response = RestAssured
                .given(spec).log().all()
                .filter(document("pathDuration",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("시작역"),
                                parameterWithName("target").description("도착역")),
                        responseFields(
                                fieldWithPath("stations").description("경로에 포함된 역 목록"),
                                fieldWithPath("stations[].id").description("지하철 역 ID"),
                                fieldWithPath("stations[].name").description("지하철 역 이름"),
                                fieldWithPath("stations[].createdDate").description("역 생성일"),
                                fieldWithPath("stations[].modifiedDate").description("역 최근 수정일"),
                                fieldWithPath("distance").description("시작역과 도착역 사이 거리"))))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths/duration")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
