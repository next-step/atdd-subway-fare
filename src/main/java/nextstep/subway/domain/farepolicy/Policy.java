package nextstep.subway.domain.farepolicy;

public interface Policy {
    int DEFAULT_OVER_CHARGE_DISTANCE = 5;
    int EXTRA_CHARGE_START_DISTANCE = 50;
    int EXTRA_CHARGE = 100;
    int EXTRA_CHARGE_DISTANCE = 8;
    int AGE_CHILD_MIN = 6;
    int AGE_YOUTH_MIN = 13;
    int AGE_ADULT_MIN = 19;
    int DEDUCTION_FARE = 350;

    /**
     * 요금을 입력받고, 해당 정책이 반영된 요금을 반환한다.
     */
    int calculate(int fare);
}
