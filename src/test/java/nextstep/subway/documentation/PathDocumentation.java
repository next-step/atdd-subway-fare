package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
import org.springframework.restdocs.request.RequestParametersSnippet;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        Long source = 2L;
        Long target = 3L;
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(new StationResponse(source, "강남역"),
                        new StationResponse(target, "역삼역")), 10);

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        final RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("source").description("출발역"),
                parameterWithName("target").description("도착역")
        );

        final RequestSpecification requestSpecification = RestAssured
                .given(spec).log().all()
                .filter(
                        document("path", requestParametersSnippet)
                );

        final ExtractableResponse<Response> response = 경로_조회_요청(requestSpecification, source,
                target);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
