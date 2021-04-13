package nextstep.subway.path.domain.fare.age;

public class AdultFareChain implements AgeChain{

    private static final int ADULT_AGE_MIN = 20;

    @Override
    public boolean findAgeGroup(int age) {
        return age >= ADULT_AGE_MIN;
    }

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
