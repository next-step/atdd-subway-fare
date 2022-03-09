package nextstep.subway.domain;

import java.util.function.Function;

public enum PathInquiryType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> expression;

    PathInquiryType(Function<Section, Integer> expression) {
        this.expression = expression;
    }

    public int edgeWeight(Section section) {
        return expression.apply(section);
    }
}
