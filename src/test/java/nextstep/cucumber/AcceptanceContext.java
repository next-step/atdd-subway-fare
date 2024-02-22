package nextstep.cucumber;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Profile("test")
@Component
public class AcceptanceContext {
    public Map<String, Object> store = new HashMap<>();
    public ExtractableResponse<Response> response;
}
