package nextstep.subway.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class Documentation {

    @LocalServerPort
    int port;

    final RestDocumentationFilter pathDocument = document(
        "path",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()),
        requestParameters(
            parameterWithName("source").description("출발역"),
            parameterWithName("target").description("도착역"),
            parameterWithName("type").description("경로 조회 타입")
        ),
        responseFields(
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로의 역들"),
            fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 아이디"),
            fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
            fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
            fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시간"),
            fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금(거리비례제로 책정)")
        )
    );

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .build();
        RestAssured.filters(pathDocument);
    }
}
