package nextstep.subway.domain;

@FunctionalInterface
public interface PathWeight {
    int getWeight(Section section);
}
