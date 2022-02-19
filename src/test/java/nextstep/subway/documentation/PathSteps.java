package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    private static final String DISTANCE_TYPE = "DISTANCE";
    private static final String DURATION_TYPE = "DURATION";

    public static ExtractableResponse<Response> 최단_거리_경로_요청(RequestSpecification spec, Long source, Long target) {
        return 최단_경로_요청(spec, source, target, DISTANCE_TYPE);
    }

    public static ExtractableResponse<Response> 최단_시간_경로_요청(RequestSpecification spec, Long source, Long target) {
        return 최단_경로_요청(spec, source, target, DURATION_TYPE);
    }

    private static ExtractableResponse<Response> 최단_경로_요청(RequestSpecification spec, Long source, Long target, String type) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getRequestFieldsSnippets(),
                        getResponseFieldsSnippet()))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(fieldWithPath("stations[].id").description("지하철 역 번호"),
                fieldWithPath("stations[].name").description("지하철 역 이름"),
                fieldWithPath("stations[].createdDate").description("생성 시각"),
                fieldWithPath("stations[].modifiedDate").description("수정 시각"),
                fieldWithPath("distance").description("총 거리"),
                fieldWithPath("duration").description("총 소요시간"));
    }

    private static RequestParametersSnippet getRequestFieldsSnippets() {
        return requestParameters(parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("검색 유형"));
    }

}
