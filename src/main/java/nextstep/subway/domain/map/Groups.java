package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Groups<K, E> implements Iterable<List<E>>{
    private final List<List<E>> groups;
    private K previousKey;

    public Groups() {
        this.groups = new ArrayList<>();
    }

    public void put(K key, E element) {
        if (notEqualsPreviousKey(key)) {
            groups.add(new ArrayList<>());
        }
        putAtLastGroup(element);
        previousKey = key;
    }

    private boolean notEqualsPreviousKey(K key) {
        return Objects.isNull(key) || Objects.isNull(previousKey) || !key.equals(previousKey);
    }

    private void putAtLastGroup(E element) {
        List<E> lastGroup = groups.get(groups.size() - 1);
        lastGroup.add(element);
    }

    public int size() {
        return groups.size();
    }


    @Override
    public Iterator<List<E>> iterator() {
        return groups.iterator();
    }
}
