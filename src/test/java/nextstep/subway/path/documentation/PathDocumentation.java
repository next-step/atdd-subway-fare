package nextstep.subway.path.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
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

    @MockBean
    private PathService pathService;
    private PathResponse 을지로3가_시청역경로;

    @Test
    void path() {


        List<StationResponse> stationResponses = Lists.newArrayList(new StationResponse(1L, "을지로3가역", LocalDateTime.now(), LocalDateTime.now())
                                                ,new StationResponse(2L, "시청역", LocalDateTime.now(), LocalDateTime.now()));
        을지로3가_시청역경로 = new PathResponse( stationResponses, 10, 10, 1_250);

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(을지로3가_시청역경로);

        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("경로검색 타입")
        );

        ResponseFieldsSnippet responseFieldsSnippet = responseFields(
                fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("최단경로 역 정보"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("최단경로 역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("최단경로 역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("최단경로 생성시간"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("최단경로 수정시간"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("최단경로 역 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("최단경로 역 시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금 정보")

        );

        RequestSpecification requestSpecification = RestAssured
                                .given(spec).log().all()
                                .filter(document("path",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        requestParametersSnippet,
                                        responseFieldsSnippet));

        두_역의_최단_거리_경로_조회를_요청(requestSpecification, 1L,2L);
    }
}

