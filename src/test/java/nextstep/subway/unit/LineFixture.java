package nextstep.subway.unit;

import nextstep.subway.domain.Line;

public class LineFixture {
    private static final Integer DEFAULT_ADDITIONAL_FARE = 0;

    public Line createLine(String name, String color) {
        return createLine(name, color, DEFAULT_ADDITIONAL_FARE);
    }

    public Line createLine(String name, String color, int additionalFare) {
        return new Line(name, color, additionalFare);
    }
}
