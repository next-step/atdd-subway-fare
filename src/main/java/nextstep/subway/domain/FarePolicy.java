package nextstep.subway.domain;

@FunctionalInterface
public interface FarePolicy<T> {
    int apply(T value, int baseFare);
}
