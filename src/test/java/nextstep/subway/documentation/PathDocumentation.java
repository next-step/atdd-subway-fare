package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.service.PathService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                BigInteger.TEN
        );

        // given
        when(pathService.getShortestPath(anyLong(), anyLong())).thenReturn(pathResponse);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given(getSpec("path", getRequestParameters(), getResponseFields())).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private RequestParametersSnippet getRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("출발 역"),
                parameterWithName("target").description("도착 역"));
    }

    private ResponseFieldsSnippet getResponseFields() {
        // stations 내 정보는 StationResponse를 사용. 이에 PathResponse는 StationResponse에 종속적.
        // StationResponse의 필드가 추가되면 PathResponse의 Restdocs에도 반영 필요 -> 유지보수 어려움.
        // then how?
        return responseFields(
                fieldWithPath("stations[].id").description("지하철 역 아이디"),
                fieldWithPath("stations[].name").description("지하철 역 이름"),
                fieldWithPath("distance").description("총 소요 거리"));
    }
}
