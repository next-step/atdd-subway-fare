package nextstep.subway.domain;

@FunctionalInterface
public interface PathType {
    double getWeight(Section section);
}
