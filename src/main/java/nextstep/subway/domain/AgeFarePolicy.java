package nextstep.subway.domain;

public class AgeFarePolicy implements FarePolicy {

    private final int age;

    public AgeFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public int calcFare(int currentFare) {
        if (isChild(age)) {
            return (currentFare - 350) / 2;
        }

        return currentFare;
    }

    private boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

}
