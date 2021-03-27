package nextstep.subway.path.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Documentation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentRequest;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
        // given
        ArrayList<StationResponse> stationResponses = Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
        );

        PathResponse pathResponse = new PathResponse(stationResponses, 10, 10);
        given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        // when & then
        두_역의_최단_거리_경로_조회를_요청(1L, 2L);
    }

    public static RequestSpecification givenRestAssured() {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getPathDocumentRequestParameters(),
                        getPathDocumentResponseFields()));
    }

    private static RequestParametersSnippet getPathDocumentRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("조회 방법")
        );
    }

    private static ResponseFieldsSnippet getPathDocumentResponseFields() {
        return responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철 역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철 역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("지하철 역 등록 날짜"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("지하철 역 최종 수정 날짜"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리 (km)"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간 (분)")
        );
    }
}

