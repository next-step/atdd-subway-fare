package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static ExtractableResponse<Response> 경로조회_및_문서_생성(RequestSpecification spec, RestDocumentationFilter filter, Map<String, String> params) {
        return RestAssured
                .given(spec).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static Map<String, String> 경로_조회_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "2");
        return params;
    }

    public static RestDocumentationFilter 경로관련_문서_필터생성(RequestParametersSnippet requestParametersSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
        RestDocumentationFilter filter = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
        return filter;
    }


}
