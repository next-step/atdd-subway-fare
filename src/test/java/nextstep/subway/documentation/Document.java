package nextstep.subway.documentation;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import nextstep.marker.Documentation;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Documentation
public abstract class Document {


    protected RequestSpecification given;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.given = new RequestSpecBuilder()
                .addFilter(getDocumentationFilter())
                .addFilter(documentationConfiguration(restDocumentation))
                .log(LogDetail.ALL)
                .build();
    }

    protected abstract RestDocumentationFilter getDocumentationFilter();
}
