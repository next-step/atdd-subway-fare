package nextstep.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("test")
public class AcceptanceContext {
    public Map<String, Object> store = new HashMap<>();
    public ExtractableResponse<Response> response;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    public ObjectMapper objectMapper;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }
}
