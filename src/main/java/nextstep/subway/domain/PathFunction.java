package nextstep.subway.domain;

@FunctionalInterface
public interface PathFunction {

    int value(Section section);

}
