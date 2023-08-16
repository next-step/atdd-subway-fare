package nextstep.domain.subway.Fare;

public abstract class FarePolicy {
    private FarePolicy nextFarePolicy = null;

    public abstract int calculateFare(int fare);

    public final FarePolicy setNextFarePolicy(FarePolicy nextFarePolicy){

        FarePolicy farePolicy = this;
        while (farePolicy.nextFarePolicy != null){
            farePolicy = farePolicy.nextFarePolicy;
        }
        farePolicy.nextFarePolicy = nextFarePolicy;

        return this;
    }

    public int getCalculatedFare(int fare){

        FarePolicy farePolicy = this;
        while (farePolicy != null){
            fare = farePolicy.calculateFare(fare);
            farePolicy = farePolicy.nextFarePolicy;
        }

        return fare;
    }

}
