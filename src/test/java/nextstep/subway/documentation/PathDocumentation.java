package nextstep.subway.documentation;

import nextstep.line.application.LineService;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.station.application.response.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static nextstep.line.acceptance.LineRequester.findShortPathForDucument;
import static nextstep.line.domain.path.ShortPathType.DISTANCE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    @Test
    void path() {

        // given
        ShortPathResponse shortPathResponse = new ShortPathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "선릉역")),
                10, 10, 1250
        );

        when(lineService.findShortPath(any(), any(), any())).thenReturn(shortPathResponse);

        // when then
        findShortPathForDucument(spec,
                getFilter("/lines/paths", getLinePathsRequestField(), getLinePathsResponseField()),
                DISTANCE,
                1L,
                2L);
    }

    private RestDocumentationFilter getFilter(String documentPath, RequestParametersSnippet requestParametersSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
        return document(documentPath,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
    }

    private RequestParametersSnippet getLinePathsRequestField() {
        return requestParameters(
                parameterWithName("startStationId").description("출발역"),
                parameterWithName("endStationId").description("도착역"),
                parameterWithName("type").description("조회타입")
        );
    }

    private ResponseFieldsSnippet getLinePathsResponseField() {
        return responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 정보"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 id"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 소요시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
        );
    }
}
