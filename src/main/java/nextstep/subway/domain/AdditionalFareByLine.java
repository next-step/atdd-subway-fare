package nextstep.subway.domain;

public class AdditionalFareByLine {

    private final int fareByLine;

    public AdditionalFareByLine(int fareByLine) {
        this.fareByLine = fareByLine;
    }

    public int fare(int prevFare) {
        return prevFare + fareByLine;
    }
}
