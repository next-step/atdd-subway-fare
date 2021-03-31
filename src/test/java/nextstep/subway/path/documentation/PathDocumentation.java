package nextstep.subway.path.documentation;

import io.restassured.builder.RequestSpecBuilder;
import nextstep.subway.Documentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.subway.path.acceptance.PathSteps.최단경로_조회_문서화;
import static nextstep.subway.path.acceptance.PathSteps.최소시간경로_조회_문서화;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

public class PathDocumentation extends Documentation {
    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void shortest_path() {
        최단경로_조회_문서화(spec);
    }

    @Test
    void fastest_path() {
        최소시간경로_조회_문서화(spec);
    }
}

