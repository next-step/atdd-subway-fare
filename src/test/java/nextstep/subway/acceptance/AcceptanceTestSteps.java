package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class AcceptanceTestSteps {

    public static RequestSpecification given() {
        return RestAssured
                .given().log().all();
    }

    public static RequestSpecification given(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token);
    }

    public static RequestSpecification given(RequestSpecification spec, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
