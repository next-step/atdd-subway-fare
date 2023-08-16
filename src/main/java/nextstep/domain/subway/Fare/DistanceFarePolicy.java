package nextstep.domain.subway.Fare;

public class DistanceFarePolicy extends FarePolicy{

    private Long distance;

    public DistanceFarePolicy(Long distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare(int fare) {

        return fare + DistanceFareType.getFareByDistance(this.distance);
    }
}
