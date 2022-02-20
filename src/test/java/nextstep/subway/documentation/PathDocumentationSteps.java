package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
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

    public static RequestSpecification createSpecification(RequestSpecification specification, RestDocumentationFilter filter) {
        return RestAssured
                .given(specification).log().all()
                .filter(filter);
    }

    public static ResponseFieldsSnippet createResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description("역 목록"),
                fieldWithPath("stations[].id").description("고유번호"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("stations[].createdDate").description("생성 시간"),
                fieldWithPath("stations[].modifiedDate").description("최종 수정 시간"),
                fieldWithPath("distance").description("전체 거리"),
                fieldWithPath("duration").description("소요 시간"),
                fieldWithPath("fare").description("거리 비례 요금"),
                fieldWithPath("extraCharge").description("추가 요금"),
                fieldWithPath("discount").description("할인 요금"),
                fieldWithPath("totalFare").description("합계 요금")
        );
    }

    public static RequestParametersSnippet createRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발 역 ID"),
                parameterWithName("target").description("도착 역 ID"),
                parameterWithName("type").description("DISTANCE: 최단 경로, DURATION: 최소 소요 시간")
        );
    }


    public static RestDocumentationFilter createFilter(RequestParametersSnippet requestParametersSnippet,
                                                 ResponseFieldsSnippet responseFieldsSnippet) {
        return document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
    }

}
