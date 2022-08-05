package nextstep.subway.domain;

@FunctionalInterface
public interface PathFunction {
    int getValue(Section section);
}
