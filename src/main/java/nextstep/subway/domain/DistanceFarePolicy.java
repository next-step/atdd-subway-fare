package nextstep.subway.domain;

public class DistanceFarePolicy extends AbstractFarePolicy {

    private final int maxExtraFare;

    public DistanceFarePolicy(int maxExtraFare) {
        this(null, maxExtraFare);
    }

    public DistanceFarePolicy(FarePolicy nextPolicy, int maxExtraFare) {
        super(nextPolicy);
        this.maxExtraFare = maxExtraFare;
    }

    @Override
    public int calculateFare(int fare) {
        int result = fare + maxExtraFare;

        if (hasNext()) {
            return nextPolicy.calculateFare(result);
        }

        return result;
    }
}
