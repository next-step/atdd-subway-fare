package nextstep.cucumber.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class AcceptanceContext {
    public Map<String, Long> store = new HashMap<>();
    public ExtractableResponse<Response> response;

    public void add(ExtractableResponse<Response> response) {
        store.put(response.jsonPath().getString("name"),
                response.jsonPath().getLong("id"));
    }

    public Long get(String name) {
        return store.get(name);
    }
}


