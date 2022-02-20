package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static nextstep.subway.acceptance.LineSteps.경로_조회됨;
import static nextstep.subway.acceptance.PathAcceptanceSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.documentation.PathDocumentationFixture.PATH_RESPONSE_FIXTURE;
import static nextstep.subway.documentation.PathDocumentationSteps.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @ParameterizedTest(name = "경로 조회 문서화 [{index}] [{arguments}]")
    @EnumSource(PathType.class)
    void path(PathType pathType) {
        //given
        when(pathService.findPath(anyInt(), anyLong(), anyLong(), any(PathType.class))).thenReturn(PATH_RESPONSE_FIXTURE);

        RequestParametersSnippet requestParameters = createRequestParametersSnippet();
        ResponseFieldsSnippet responseFields = createResponseFieldsSnippet();

        RestDocumentationFilter filter = createFilter(requestParameters, responseFields);
        RequestSpecification specification = createSpecification(spec, filter);

        //when
        ExtractableResponse<Response> 경로_조회_응답 = 두_역의_경로_조회를_요청(specification, 1L, 2L, pathType);

        //then
        경로_조회됨(경로_조회_응답, HttpStatus.OK);
    }

}
