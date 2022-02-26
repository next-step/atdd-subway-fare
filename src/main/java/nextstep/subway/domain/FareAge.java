package nextstep.subway.domain;

public class FareAge {
    private final int deductible;
    private final int discountRate;
    private FareAge(int deductible, int discountRate) {
        this.deductible = deductible;
        this.discountRate = discountRate;
    }

    public static FareAge valueOf(int age) {
        if (isChild(age)) {
            return new FareAge(350, 50);
        }
        if (isTeenager(age)) {
            return new FareAge(350, 20);
        }
        return createGeneral();
    }

    private static boolean isChild(int age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenager(int age) {
        return age >= 13 && age < 19;
    }

    private static FareAge createGeneral() {
        return new FareAge(0, 0);
    }

    public int getFareAge(int fare) {
        int deductibleFare = (fare - deductible);
        return (int) (deductibleFare - (deductibleFare * (discountRate * 0.01)));
    }
}
