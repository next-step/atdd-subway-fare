package nextstep.subway.cucumber;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class AcceptanceContext {

    private Map<String, String> store = new HashMap<>();

    public void put(final String name, final String id) {
        store.putIfAbsent(name, id);
    }

    public String get(final String name) {
        return store.get(name);
    }

    public Long getLongValue(final String name) {
        return Long.valueOf(store.get(name));
    }
}
