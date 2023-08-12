package nextstep.subway.domain.fare;

import nextstep.subway.domain.fare.FarePolicy;

public class LineFarePolicy extends FarePolicy {

    private final int fareByLine;

    public LineFarePolicy(int fareByLine) {
        this.fareByLine = fareByLine;
    }

    @Override
    public int fare(int prevFare) {
        return prevFare + fareByLine;
    }
}
