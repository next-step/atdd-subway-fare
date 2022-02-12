package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import static nextstep.subway.acceptance.PathSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RequestParametersSnippet requestParametersSnippet = getRequestParameters();
        ResponseFieldsSnippet responseFieldsSnippet = getResponseFields();


        RestDocumentationFilter filter = 경로관련_문서_필터생성(requestParametersSnippet, responseFieldsSnippet);

        //when
        ExtractableResponse<Response> response = 경로조회_및_문서_생성(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }

    private ResponseFieldsSnippet getResponseFields() {
        ResponseFieldsSnippet responseFieldsSnippet = responseFields(
                    fieldWithPath("stations").description("해당 경로에 포함된 역 목록"),
                    fieldWithPath("stations[].id").description("지하철 역의 ID (Long)"),
                    fieldWithPath("stations[].name").description("지하철 역 이름"),
                    fieldWithPath("stations[].createdDate").description("해당 역 생성 일"),
                    fieldWithPath("stations[].modifiedDate").description("해당 역 마지막 수정 일"),
                    fieldWithPath("distance").description("해당 경로 사이의 거리")
        );
        return responseFieldsSnippet;
    }

    private RequestParametersSnippet getRequestParameters() {
        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("source").description("조회하는 경로에 상행역"),
                parameterWithName("target").description("조회하는 경로에 하행역")
        );
        return requestParametersSnippet;
    }

    private PathResponse getPathResponse() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10
        );
        return pathResponse;
    }

    private void 경로조회_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("강남역", "역삼역");
    }


}
