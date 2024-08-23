package nextstep.path.dto;

import nextstep.member.domain.Member;

public class CalculateMemberAgeFare {

    private static final int MINIMUM_PRICE_FOR_DISCOUNT = 350;
    private static final int MIN_AGE_FOR_DISCOUNT_CHILD = 6;
    private static final int MAX_AGE_FOR_DISCOUNT_CHILD = 13;
    private static final int MAX_AGE_FOR_DISCOUNT = 18;
    private static final int DISCOUNT_PERCENT_CHILD = 50;
    private static final int DISCOUNT_PERCENT_ADULT = 20;
    private static final int DEFAULT_DISCOUNT_PERCENT = 0;
    private static final double PERCENTAGE_FACTOR = 0.01;

    public static Path of(Path path) {
        Long totalPrice = calculateMemberAge(path.getMember(), path.getTotalPrice());
        path.setTotalPrice(totalPrice);
        return path;
    }

    public static Long calculateMemberAge(final Member member, final Long totalPrice) {
        if (member == null) {
            return totalPrice;
        }

        int age = member.getAge();

        if (totalPrice < MINIMUM_PRICE_FOR_DISCOUNT) {
            return totalPrice;
        }

        double discountPercent = getDiscountPercent(age);
        long discountAmount = (long) (((totalPrice - MINIMUM_PRICE_FOR_DISCOUNT) * discountPercent));

        return totalPrice - discountAmount;
    }

    private static double getDiscountPercent(final int age) {
        if (age < MIN_AGE_FOR_DISCOUNT_CHILD || age > MAX_AGE_FOR_DISCOUNT) {
            return DEFAULT_DISCOUNT_PERCENT;
        }

        if (age >= MIN_AGE_FOR_DISCOUNT_CHILD && age < MAX_AGE_FOR_DISCOUNT_CHILD) {
            return DISCOUNT_PERCENT_CHILD * PERCENTAGE_FACTOR;
        }

        return DISCOUNT_PERCENT_ADULT * PERCENTAGE_FACTOR;
    }
}

