package nextstep.subway.documentation.step;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentSteps {

    public static RestDocumentationFilter 경로_조회_문서화() {
        return document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                경로_조회_요청_정의(),
                경로_조회_응답_정의()
        );
    }

    public static PathResponse 경로_조회_응답_생성(List<Station> stations) {
        LocalDateTime now = LocalDateTime.now();
        List<StationResponse> stationResponses = stations.stream()
                .map(it -> new StationResponse(it.getId(), it.getName(), now, now))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, 10);
    }

    private static ResponseFieldsSnippet 경로_조회_응답_정의() {
        return responseFields(
                fieldWithPath("stations")
                        .type(ARRAY)
                        .description("지하철역"),
                fieldWithPath("stations[].id")
                        .type(NUMBER)
                        .description("지하철역 ID"),
                fieldWithPath("stations[].name")
                        .type(STRING)
                        .description("지하철역 이름"),
                fieldWithPath("stations[].createdDate")
                        .type(STRING)
                        .description("지하철역 생성일"),
                fieldWithPath("stations[].modifiedDate")
                        .type(STRING)
                        .description("지하철역 수정일"),
                fieldWithPath("distance")
                        .type(NUMBER)
                        .description("거리")
        );
    }

    private static RequestParametersSnippet 경로_조회_요청_정의() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"));
    }

}
