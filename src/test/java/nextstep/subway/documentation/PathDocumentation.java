package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.FindType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.time.LocalDateTime;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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

    private PathResponse pathResponse;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 6
        );
        when(pathService.findPath(anyLong(), anyLong(), any(FindType.class))).thenReturn(pathResponse);
    }

    @Test
    void path() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회_요청(
                pathDocumentationConfig(RestAssured.given(spec), "path"),
                1L, 2L, "DISTANCE");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void pathDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회_요청(
                pathDocumentationConfig(RestAssured.given(spec), "pathDuration"),
                1L, 2L, "DURATION");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private RequestSpecification pathDocumentationConfig(RequestSpecification given, String documentPath) {
        return given.filter(document(documentPath,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("시작역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("조회타입")),
                        responseFields(
                                fieldWithPath("stations").description("경로에 포함된 역 목록"),
                                fieldWithPath("stations[].id").description("지하철 역 ID"),
                                fieldWithPath("stations[].name").description("지하철 역 이름"),
                                fieldWithPath("stations[].createdDate").description("역 생성일"),
                                fieldWithPath("stations[].modifiedDate").description("역 최근 수정일"),
                                fieldWithPath("distance").description("시작역과 도착역 사이 거리"),
                                fieldWithPath("duration").description("총 소요시간"))));
    }
}
