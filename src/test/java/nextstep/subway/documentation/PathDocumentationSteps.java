package nextstep.subway.documentation;

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
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathDocumentationSteps {

    public static RestDocumentationFilter getSearchPathDocumentFilter(String documentName) {
          return document(documentName,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("source").description("출발역"),
                        parameterWithName("target").description("도착역"),
                        parameterWithName("pathSearchType").description("경로 조회 타입")),
                    responseFields(
                        fieldWithPath("stations").description("역 목록"),
                        fieldWithPath("stations[].id").description("지하철 역 ID"),
                        fieldWithPath("stations[].name").description("지하철 역 이름"),
                        fieldWithPath("stations[].createdDate").description("지하철 역 생성 일자"),
                        fieldWithPath("stations[].modifiedDate").description("지하철 역 수정 일자"),
                        fieldWithPath("distance").description("총 거리"),
                        fieldWithPath("duration").description("소요 시간")
                    ));
    }

    public static RequestSpecification getFilteredRequestSpecification(RequestSpecification spec, RestDocumentationFilter filter) {
        return RestAssured.given(spec)
            .log().all().filter(filter);
    }
}
