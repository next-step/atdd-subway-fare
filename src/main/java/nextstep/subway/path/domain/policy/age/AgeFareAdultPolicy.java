package nextstep.subway.path.domain.policy.age;

public class AgeFareAdultPolicy implements AgeFarePolicy {

    private final int fare;

    public AgeFareAdultPolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculateAgeFare() {
        return fare;
    }
}
