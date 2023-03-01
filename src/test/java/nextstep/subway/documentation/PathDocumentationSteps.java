package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentationSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification spec, Long source, Long target) {
        return RestAssured
                .given(spec).log().all()
                .filter(getDocument("path"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static RestDocumentationFilter getDocument(String identifier) {
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                getRequestParametersSnippet(),
                getResponseFieldsSnippet()
        );
    }

    private static ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("지하철역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 id"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리")
        );
    }

    private static RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역 id"),
                parameterWithName("target").description("도착역 id")
        );
    }
}
