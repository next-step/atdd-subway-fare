package nextstep.subway.domain;

@FunctionalInterface
public interface PathCostFunction {
    int cost(Section section);
}
