package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Lines;

public class LineExtraFare implements FarePolicy {

    private final Lines lines;

    public LineExtraFare(Lines lines) {
        this.lines = lines;
    }

    @Override
    public long getFare() {
        return lines.calculatePlusExtraFare();
    }

}
