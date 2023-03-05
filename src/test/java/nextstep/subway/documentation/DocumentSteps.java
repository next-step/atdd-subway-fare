package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class DocumentSteps {
    static RequestSpecification given(RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all();
    }

    static RequestSpecification given(RequestSpecification spec, String token) {
        return RestAssured
                .given(spec).log().all()
                .auth().oauth2(token);
    }
}
