package nextstep.subway.path.domain;

import nextstep.subway.exceptions.NotFoundAgeTypeException;

import java.util.Arrays;
import java.util.function.Function;

public enum CostByAge {
    BABY(age -> age > 0 && age < 6, 1),
    KID(age -> age >= 6 && age < 13, 0.5),
    YOUTH(age -> age >= 13 && age < 19, 0.2),
    ADULT(age -> age >= 19 || age == 0, 0);

    private final Function<Integer, Boolean> function;
    private final double discountRate;

    private static final Long DEDUCTIONS_AMOUNT = 350L;

    CostByAge(Function<Integer, Boolean> function, double discountRate) {
        this.function = function;
        this.discountRate = discountRate;
    }

    public static Cost applyDiscount(Cost cost, int age) {
        CostByAge foundType = Arrays.stream(CostByAge.values())
                .filter(costByAge -> costByAge.function.apply(age))
                .findFirst()
                .orElseThrow(NotFoundAgeTypeException::new);

        return resultCost(cost, foundType.discountRate);
    }


    private static Cost resultCost(Cost cost, double discountRate) {
        long discountCost = (long) Math.floor((cost.getCost() - DEDUCTIONS_AMOUNT) * discountRate);
        return new Cost(cost.getCost() - discountCost);
    }
}
