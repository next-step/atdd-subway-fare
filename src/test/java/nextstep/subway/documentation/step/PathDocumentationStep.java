package nextstep.subway.documentation.step;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathDocumentationStep {

    public static RestDocumentationFilter 경로_조회_문서화() {
        return document("path",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("atdd-subway-fare")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            경로_조회_요청_정의(),
            경로_조회_응답_정의()
        );
    }

    public static PathResponse 경로_조회_응답_생성() {
        return new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10, 5, 1250
        );
    }

    public static RequestParametersSnippet 경로_조회_요청_정의() {
        return requestParameters(
            parameterWithName("source").description("출발역 ID"),
            parameterWithName("target").description("도착역 ID"),
            parameterWithName("pathType").description("조회 타입")
        );
    }

    public static ResponseFieldsSnippet 경로_조회_응답_정의() {
        return responseFields(
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로역"),
            fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
            fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역명"),
            fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
            fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시간"),
            fieldWithPath("price").type(JsonFieldType.NUMBER).description("요금")
        );
    }

}
