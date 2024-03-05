package nextstep.cucumber;

import java.util.HashMap;
import java.util.Map;

public class CucumberStore {
    private final Map<String, Object> store;

    public CucumberStore() {
        store = new HashMap<>();
    }

    public Object put(final String key, final Object value) {
        return store.put(key, value);
    }

    public <V> V get(final String key, final Class<V> clazz) {
        if (!store.containsKey(key)) {
            throw new IllegalArgumentException(String.format("key - %s is not exist", key));
        }
        return clazz.cast(store.get(key));
    }

    public <V> V getOrDefault(final String key, final Class<V> clazz, final V defaultValue) {
        if (!store.containsKey(key)) {
            return defaultValue;
        }
        return clazz.cast(store.get(key));
    }
}
