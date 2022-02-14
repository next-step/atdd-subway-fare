package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(createPathResponse());

        RequestSpecification spec = RestAssured
                .given(this.spec)
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getRequestParameters(),
                        getResponseFields()
                ));

        PathSteps.두_역의_최단_거리_경로_조회를_요청(spec, 1L, 2L);
    }

    private static RequestParametersSnippet getRequestParameters() {
        String formattedEnumValues = Arrays.stream(PathType.values())
                .map(type -> String.format("`%s`", type))
                .collect(Collectors.joining(", "));

        return requestParameters(
                parameterWithName("source").description("출발역"),
                parameterWithName("target").description("도착역"),
                parameterWithName("type").description("타입 목록: " + formattedEnumValues + ".")
        );
    }

    private static ResponseFieldsSnippet getResponseFields() {
        return responseFields(
                fieldWithPath("stations").description("경로에 포함된 역의 목록"),
                fieldWithPath("stations[].id").description("id"),
                fieldWithPath("stations[].name").description("이름"),
                fieldWithPath("stations[].createdDate").description("생성 수정 날짜"),
                fieldWithPath("stations[].modifiedDate").description("최종 수정 날짜"),
                fieldWithPath("distance").description("경로 거리"),
                fieldWithPath("duration").description("경로 소요 시간")
        );
    }

    private PathResponse createPathResponse() {
        return new PathResponse(
                Arrays.asList(
                        station(1L, "강남역"),
                        station(2L, "양재역")
                ),
                1, 2, 100
        );
    }

    private StationResponse station(long id, String station) {
        return new StationResponse(id, station, LocalDateTime.now(), LocalDateTime.now());
    }
}
