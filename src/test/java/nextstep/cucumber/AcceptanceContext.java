package nextstep.cucumber;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class AcceptanceContext {

  public Map<String, Object> store = new HashMap<>();
  public ExtractableResponse<Response> response;

  public Long getLong(final String key) {
    return (Long) store.get(key);
  }

  public void clear() {
    store.clear();
    response = null;
  }
}
