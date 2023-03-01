package nextstep.subway.domain;

public class ExtraSectionFarePolicy implements FarePolicy {

    private final int maxSectionFare;

    public ExtraSectionFarePolicy(int maxSectionFare) {
        this.maxSectionFare = maxSectionFare;
    }

    @Override
    public int calcFare(int currentFare) {
        return currentFare + maxSectionFare;
    }

}
