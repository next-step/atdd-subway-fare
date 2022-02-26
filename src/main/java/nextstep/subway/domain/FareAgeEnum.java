package nextstep.subway.domain;

public enum FareAgeEnum {
    CHILD(350, 50, 6, 13),
    TEENAGER(350, 20, 13, 19),
    GENERAL(0, 0, 0, 0);

    private final int deductibleAmount;
    private final int discountRate;
    private final int beginAge;
    private final int endAge;

    FareAgeEnum(int deductibleAmount, int discountRate, int beginAge, int endAge) {
        this.deductibleAmount = deductibleAmount;
        this.discountRate = discountRate;
        this.beginAge = beginAge;
        this.endAge = endAge;
    }

    public int getFareAge(int fare) {
        int deductibleFare = (fare - deductibleAmount);
        return (int) (deductibleFare - (deductibleFare * (discountRate * 0.01)));
    }

    public static FareAgeEnum valueOf(int age) {
        FareAgeEnum[] values = values();
        for (FareAgeEnum value : values) {
            if (age >= value.beginAge && age < value.endAge) {
                return value;
            }
        }
        return GENERAL;
    }


}
