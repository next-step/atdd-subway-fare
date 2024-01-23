package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Profile("test")
@Component
public class AcceptanceContext {
    private final Map<String, Object> store = new HashMap<>();

    public void put(String key, Object value) {
        this.store.put(key, value);
    }

    public void clear(){
        this.store.clear();
    }

    public Object get(String key) {
        return this.store.get(key);
    }
}
