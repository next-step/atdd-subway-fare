package nextstep.subway.documentation.step;

import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineSectionDocumentSteps {

    public static RestDocumentationFilter 구간_생성_문서화() {
        return document("section",
                preprocessRequest(prettyPrint()),
                구간_생성_요청_정의(),
                구간_생성_PathParam_정의()
        );
    }

    private static PathParametersSnippet 구간_생성_PathParam_정의() {
        return pathParameters(parameterWithName("lineId").description("노선 ID"));
    }

    private static RequestFieldsSnippet 구간_생성_요청_정의() {
        return requestFields(
                fieldWithPath("upStationId").description("상행역 ID"),
                fieldWithPath("downStationId").description("하행역 ID"),
                fieldWithPath("distance").description("거리"),
                fieldWithPath("duration").description("시간")
        );
    }

}
