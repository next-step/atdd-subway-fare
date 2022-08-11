package nextstep.subway.domain.strategy;

import nextstep.subway.domain.Section;

@FunctionalInterface
public interface WeightStrategy {

    int weight(Section section);
}
