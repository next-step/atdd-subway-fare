package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class Documentation {

    @LocalServerPort
    private int port;

    protected RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    public RequestSpecification basicDocumentSpec(final String path) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(path,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    public RequestSpecification pathParametersDocumentSpec(final String path) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(path,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("경로 조회 타입")
                        ),
                        responseFields(
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 ID"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 총 거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로 총 시간"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("이용 요금")
                        )
                ));
    }
}
