package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentationSteps {
    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(prettyPrint());
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

    public static RequestParametersSnippet getRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("pathType").description("DISTANCE: 최단 거리, DURATION: 최소 소요 시간")
        );
    }

    public static ResponseFieldsSnippet getResponseFields() {
        return responseFields(
                fieldWithPath("stations").description("역 목록"),
                fieldWithPath("stations[].id").description("역 ID"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("stations[].createdDate").description("역 정보 생성 일자"),
                fieldWithPath("stations[].modifiedDate").description("역 정보 최종 수정 일자"),
                fieldWithPath("distance").description("전체 거리"),
                fieldWithPath("duration").description("소요 시간"),
                fieldWithPath("fare").description("총 요금"),
                fieldWithPath("basicFare").description("기본 요금"),
                fieldWithPath("lineOverFare").description("노선 추가 금액"),
                fieldWithPath("distanceOverFare").description("거리 추가 금액"),
                fieldWithPath("memberDiscountFare").description("사용자 할인 금액")
        );
    }

    public static RestDocumentationFilter getDocumentFilter(RequestParametersSnippet requestParameters,
                                                            ResponseFieldsSnippet responseFields) {
        return document("path",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters,
                responseFields);
    }

    public static RequestSpecification getSpecification(RequestSpecification spec, RestDocumentationFilter filter) {
        return RestAssured.given(spec).filter(filter);
    }
}
