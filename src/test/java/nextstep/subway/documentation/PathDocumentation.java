package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.snippet.Snippet;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
        String accessToken = 베어러_인증_로그인_요청("email4@email.com", "password").jsonPath().getString("accessToken");
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 5, 1250
        );

        when(pathService.findPath(any(UserPrincipal.class), anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        createRequestHeaders(),
                        createPathRequestParameters(),
                        createPathResponseFields()
                ))
                .header("Authorization", "bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }

    private Snippet createPathRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("Source station Id"),
                parameterWithName("target").description("Target station Id"),
                parameterWithName("type").description("Path type is DISTANCE or DURATION"));
    }

    private Snippet createPathResponseFields() {
        return responseFields(
                fieldWithPath("stations").description("Station list"),
                fieldWithPath("stations[].id").description("Station Id"),
                fieldWithPath("stations[].name").description("Station name"),
                fieldWithPath("distance").description("Distance between source and target station"),
                fieldWithPath("duration").description("Duration between source and target station"),
                fieldWithPath("fare").description("Fare between source and target station"));
    }

    private Snippet createRequestHeaders() {
        return requestHeaders(headerWithName("Authorization").description("Basic auth credentials"));
    }
}
