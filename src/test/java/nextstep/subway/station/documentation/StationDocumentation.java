package nextstep.subway.station.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class StationDocumentation extends Documentation {

    private Map<String, String> createParams;

    @BeforeEach
    void setUp() {
        createParams = new HashMap<>();
        createParams.put("name", "신촌역");
    }

    @Test
    void createStation() {
        RestAssured
                .given(spec).log().all()
                .filter(document("stations",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                 ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createParams)
                .when().post("/stations")
                .then().log().all().extract();
    }



}
