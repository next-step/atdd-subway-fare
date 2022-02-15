package nextstep.subway.documentation.step;

import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentSteps {

    public static RestDocumentationFilter 노선_생성_문서화() {
        return document("line",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                노선_생성_요청_정의(),
                노성_생성_응답_헤더_정의(),
                노선_생성_body_응답_정의()
        );
    }

    private static ResponseHeadersSnippet 노성_생성_응답_헤더_정의() {
        return responseHeaders(headerWithName("Location").description("생성 리소스 uri"));
    }

    private static RequestFieldsSnippet 노선_생성_요청_정의() {
        return requestFields(
                fieldWithPath("name").description("노선 이름"),
                fieldWithPath("color").description("노선 색"),
                fieldWithPath("upStationId").description("상행역 ID"),
                fieldWithPath("downStationId").description("하행역 ID"),
                fieldWithPath("distance").description("시간"),
                fieldWithPath("duration").description("시간")
        );
    }

    private static ResponseFieldsSnippet 노선_생성_body_응답_정의() {
        return responseFields(
                fieldWithPath("id")
                        .type(NUMBER)
                        .description("노선 ID"),
                fieldWithPath("name")
                        .type(STRING)
                        .description("노선 이름"),
                fieldWithPath("color")
                        .type(STRING)
                        .description("노선 색"),
                fieldWithPath("createdDate")
                        .type(STRING)
                        .description("노선 생성일"),
                fieldWithPath("modifiedDate")
                        .type(STRING)
                        .description("노선 수정일"),

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
                        .description("지하철역 수정일")
        );
    }

}
