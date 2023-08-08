package nextstep.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class RestAssuredUtils {
    private RestAssuredUtils() {
    }

    public static RequestSpecification given_절_생성() {
        return RestAssured.given().log().all();
    }

    public static RequestSpecification given_절_생성(RequestSpecification spec, RestDocumentationFilter document) {
        return RestAssured.given(spec).log().all()
                .filter(document);
    }
}
