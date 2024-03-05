package nextstep;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public abstract class RestAssuredRequestBuilder {

    private static final String BEARER = "Bearer ";

    protected RequestSpecification spec;
    protected String accessToken;

    public RestAssuredRequestBuilder() {
        this.spec = RestAssured.given().log().all();
    }

    public void setAuthorization() {
        if (accessToken != null && !accessToken.isEmpty()) {
            this.spec.header(AUTHORIZATION, BEARER + accessToken);
        }
    }
}
