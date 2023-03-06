package nextstep.subway.domain;

@FunctionalInterface
public interface DistanceFareFormula {
    int calculate(int distance);
}
