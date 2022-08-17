package nextstep.subway.domain;

public abstract class FarePolicy {

    private FarePolicy next;

    public FarePolicy(FarePolicy next) {
        this.next = next;
    }

    public int proceed(int fare) {

        int calculatedFare = calculateIfApplicable(fare);

        if (next != null) {
            return next.proceed(calculatedFare);
        }

        return calculatedFare;
    }

    private int calculateIfApplicable(int fare) {
        if (!applicable()) {
            return fare;
        }
        return calculate(fare);
    }

    public abstract boolean applicable();

    public abstract int calculate(int fare);
}
