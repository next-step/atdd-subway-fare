package nextstep.subway.domain;

public class AgeFarePolicy extends FarePolicy {

    private static final int DEDUCTED_FARE = 350;

    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    private final int age;

    public AgeFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public boolean applicable() {
        validateAge();
        return isChildren() || isTeenager();
    }

    private void validateAge() {
        if (age <= 0) {
            throw new IllegalArgumentException("잘못된 나이로 요금을 계산할 수 없습니다.");
        }
    }

    @Override
    public int calculate(int fare) {

        if (isChildren()) {
            return (int) ((fare - DEDUCTED_FARE) * CHILDREN_DISCOUNT_RATE);
        }

        return (int) ((fare - DEDUCTED_FARE) * TEENAGER_DISCOUNT_RATE);
    }

    private boolean isChildren() {
        return age >= 6 && age < 13;
    }

    private boolean isTeenager() {
        return age >= 13 && age < 19;
    }
}
