package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.FareAgeGroup;
import nextstep.subway.domain.Lines;

public class LineExtraFare implements FarePolicy {

    private final Lines lines;
    private final FareAgeGroup fareAgeGroup;

    public LineExtraFare(Lines lines, FareAgeGroup fareAgeGroup) {
        this.lines = lines;
        this.fareAgeGroup = fareAgeGroup;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public long getFare() {
        return lines.calculatePlusExtraFare(fareAgeGroup);
    }

}
