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
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        // given
        when(pathService.findPath(anyLong(), anyLong()))
                .thenReturn(getMockPathResponse());
        // when
        ExtractableResponse<Response> response = RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디")
                                ),
                        responseFields(
                                subsectionWithPath("stations[]").description("출발역부터 도착역까지 역 경로"),
                                fieldWithPath("distance").description("출발역과 도착역의 거리")
                        )
                        ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();
        // then
        assertThat(response.jsonPath().getLong("stations[0].id")).isEqualTo(1L);
        assertThat(response.jsonPath().getLong("stations[1].id")).isEqualTo(2L);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);

    }

    private PathResponse getMockPathResponse() {
        return new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "분당역", LocalDateTime.now(), LocalDateTime.now()))
                ,10);
    }
}
