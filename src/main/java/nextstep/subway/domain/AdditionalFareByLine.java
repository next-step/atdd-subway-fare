package nextstep.subway.domain;

public class AdditionalFareByLine implements FarePolicy {

    private final int fareByLine;

    private FarePolicy next;

    public AdditionalFareByLine(int fareByLine) {
        this.fareByLine = fareByLine;
    }

    @Override
    public void setNext(FarePolicy next) {
        this.next = next;
    }

    @Override
    public int fare(int prevFare) {

        int fare = prevFare + fareByLine;

        return next != null ? next.fare(fare) : fare;
    }
}
