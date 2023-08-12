package nextstep.subway.domain;

public class AdditionalFareByLine extends FarePolicy {

    private final int fareByLine;

    public AdditionalFareByLine(int fareByLine) {
        this.fareByLine = fareByLine;
    }

    @Override
    public int fare(int prevFare) {
        return prevFare + fareByLine;
    }
}
