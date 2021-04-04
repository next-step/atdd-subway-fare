package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum AgeFarePolicy {

    ADULT(0, 0, 19, Integer.MAX_VALUE),
    TEEN(350, 0.2, 13, 18),
    CHILD(350, 0.5, 6, 13),
    BABY(350, 0.7, 0, 5);

    private int deduction;
    private double discount;
    private int ageFrom;
    private int ageTo;

    AgeFarePolicy(int deduction, double discount, int ageFrom, int ageTo) {
        this.deduction = deduction;
        this.discount = discount;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
    }

    public static AgeFarePolicy getMatchingAgeFarePolicy(int age) {
        return Arrays.stream(values()).filter(ageFarePolicy ->
                ageFarePolicy.getAgeFrom() <= age && ageFarePolicy.getAgeTo() >= age
        ).findFirst().orElseThrow(NoSuchElementException::new);
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscount() {
        return discount;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }
}
