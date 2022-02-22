package nextstep.subway.unit;

import nextstep.subway.domain.Line;

import java.math.BigDecimal;

public class LineFixture {
    private static final BigDecimal DEFAULT_ADDITIONAL_FARE = BigDecimal.ZERO;

    public Line createLine(String name, String color) {
        return createLine(name, color, DEFAULT_ADDITIONAL_FARE);
    }

    public Line createLine(String name, String color, BigDecimal additionalFare) {
        return new Line(name, color, additionalFare);
    }
}
