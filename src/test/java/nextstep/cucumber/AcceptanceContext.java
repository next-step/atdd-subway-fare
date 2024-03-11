package nextstep.cucumber;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AcceptanceContext {

    public Map<String, Object> store = new HashMap<>();
    public ExtractableResponse<Response> response;
}