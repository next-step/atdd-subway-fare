package nextstep.member.domain;

public enum MemberAgePolicy {
    ADULT(0, 0),
    YOUTH(350, 0.2),
    CHILD(350, 0.5),
    YOUNG_CHILD(0, 1),
    UNKNOWN(0, 0),
    ;

    private final int deductibleAmount;
    private final double discountRate;

    MemberAgePolicy(final int deductibleAmount, final double discountRate) {
        this.deductibleAmount = deductibleAmount;
        this.discountRate = discountRate;
    }

    public static MemberAgePolicy of(final int age) {
        if (age >= 19) {
            return ADULT;
        }
        if (age >= 13) {
            return YOUTH;
        }
        if (age >= 6) {
            return CHILD;
        }

        return YOUNG_CHILD;
    }

    public int discountFare(final int fare) {
        return (int) ((fare - deductibleAmount) * (1 - discountRate));
    }
}
