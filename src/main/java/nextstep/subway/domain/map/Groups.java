package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.List;

public class Groups<K, E> {
    private final List<List<E>> elements;

    public Groups() {
        this.elements = new ArrayList<>();
    }

    public void put(K tempIdentifier, E element) {
        assert tempIdentifier != null;
    }

    public int size() {
        return elements.size();
    }
}
