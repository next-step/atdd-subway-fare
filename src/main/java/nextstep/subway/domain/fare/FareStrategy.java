package nextstep.subway.domain.fare;


import nextstep.member.domain.AgeType;

public abstract class FareStrategy {

    public int calculateWithAge(int distance, int age) {
        int normalFare = calculate(distance);
        return AgeType.of(age).discount(normalFare);
    }

    public abstract int calculate(int distance);
}
