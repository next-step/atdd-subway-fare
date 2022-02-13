package nextstep.subway.acceptance;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathSteps {

    public static RequestSpecification 즐겨찾기_조회하기_문서화_스펙_정의(RestDocumentationContextProvider restDocumentation) {
        return new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .addFilter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("경로조회의 시작역 아이디"),
                    parameterWithName("target").description("경로조회의 도착역 아이디")),
                responseFields(
                    fieldWithPath("stations[].id").type(Long.class).description("역 아이디"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                    fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("역 생성날짜"),
                    fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("역 수정날짜"),
                    fieldWithPath("distance").type(Integer.class).description("경로조회 총 거리"),
                    fieldWithPath("duration").type(Integer.class).description("경로조회 총 소요시간")
                )))
            .build();
    }
}
