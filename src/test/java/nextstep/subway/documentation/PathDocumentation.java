package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.documentation.PathDocumentationFixture.PATH_RESPONSE_FIXTURE;
import static nextstep.subway.documentation.PathDocumentationSteps.경로_조회_됨;
import static nextstep.subway.documentation.PathDocumentationSteps.경로_조회_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    public static final String DOCUMENT_NAME = "path";

    @MockBean
    private PathService pathService;

    @ParameterizedTest(name = "경로 조회 문서화 [{index}] [{arguments}]")
    @EnumSource(PathType.class)
    void path(PathType pathType) {
        //given
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(PATH_RESPONSE_FIXTURE);

        RequestParametersSnippet requestParameters = createRequestParametersSnippet();
        ResponseFieldsSnippet responseFields = createResponseFieldsSnippet();

        RestDocumentationFilter filter = createFilter(requestParameters, responseFields);
        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 3L);
        params.put("type", pathType);

        //when
        ExtractableResponse<Response> 경로_조회_응답 = 경로_조회_요청(spec, filter, params);

        //then
        경로_조회_됨(경로_조회_응답);
    }

    private ResponseFieldsSnippet createResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description("역 목록"),
                fieldWithPath("stations[].id").description("고유번호"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("stations[].createdDate").description("생성 시간"),
                fieldWithPath("stations[].modifiedDate").description("최종 수정 시간"),
                fieldWithPath("distance").description("전체 거리"),
                fieldWithPath("duration").description("소요 시간"),
                fieldWithPath("fare").description("요금")
        );
    }

    private RequestParametersSnippet createRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발 역 ID"),
                parameterWithName("target").description("도착 역 ID"),
                parameterWithName("type").description("DISTANCE: 최단 경로, DURATION: 최소 소요 시간")
        );
    }


    private RestDocumentationFilter createFilter(RequestParametersSnippet requestParametersSnippet,
                                                 ResponseFieldsSnippet responseFieldsSnippet) {
        return document(DOCUMENT_NAME,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
    }

}
