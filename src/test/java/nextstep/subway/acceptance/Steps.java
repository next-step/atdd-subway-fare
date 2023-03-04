package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Steps {

    public static RequestSpecification defaultRequestSpecification() {
        return RestAssured
                .given().log().all();
    }
}
