package nextstep.subway.domain;

public class AgeFarePolicy implements FarePolicy {

    @Override
    public int fare(int age, int requestFare, Path path) {
        FareAgeEnum fareAgeEnum = FareAgeEnum.valueOf(age);
        return fareAgeEnum.getFareAge(requestFare);
    }
}
