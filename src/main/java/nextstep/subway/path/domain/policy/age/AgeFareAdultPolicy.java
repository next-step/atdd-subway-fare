package nextstep.subway.path.domain.policy.age;

public class AgeFareAdultPolicy implements AgeFarePolicy {

    @Override
    public int calculateAgeFare(int fare) {
        return fare;
    }
}
