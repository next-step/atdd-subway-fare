package nextstep.cucumber;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class AcceptanceContext {
    public CucumberStore store = new CucumberStore();
    public ExtractableResponse<Response> response;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }
}
