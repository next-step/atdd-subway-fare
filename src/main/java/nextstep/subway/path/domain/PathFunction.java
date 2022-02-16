package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

@FunctionalInterface
public interface PathFunction {
    int value(Section section);
}
