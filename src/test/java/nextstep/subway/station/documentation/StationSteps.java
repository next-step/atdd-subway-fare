package nextstep.subway.station.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class StationSteps {
    public static RequestSpecification getSpec(RequestSpecification spec, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
