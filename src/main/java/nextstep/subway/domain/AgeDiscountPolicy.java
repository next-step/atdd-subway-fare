package nextstep.subway.domain;

import nextstep.member.domain.LoginMember;

import static nextstep.member.domain.RoleType.ROLE_ANONYMOUS;

public class AgeDiscountPolicy implements DiscountPolicy {

    public static final int DEDUCTION = 350;

    public static final double CHILD_DISCOUNT = 0.5;
    public static final double TEENAGER_DISCOUNT = 0.8;

    public static final int CHILD_AGE = 6;
    public static final int TEENAGER_AGE = 13;
    public static final int ADULT_AGE = 19;

    private final LoginMember member;

    public AgeDiscountPolicy(LoginMember member) {
        this.member = member;
    }

    @Override
    public int calculator(final int fare) {
        if (member.getRoles().contains(ROLE_ANONYMOUS.name())) {
            return fare;
        }

        if (member.getAge() < 1) {
            throw new IllegalArgumentException("나이는 0 이하일 수 없습니다.");
        }

        return getDiscountFare(fare, member.getAge());
    }

    private int getDiscountFare(final int fare, final int age) {
        if (isTeenager(age)) {
            return calculateDiscountFare(fare, TEENAGER_DISCOUNT);
        } else if (isChild(age)) {
            return calculateDiscountFare(fare, CHILD_DISCOUNT);
        }

        return fare;
    }

    private boolean isChild(final int age) {
        return age >= CHILD_AGE && age < TEENAGER_AGE;
    }

    private boolean isTeenager(final int age) {
        return age >= TEENAGER_AGE && age < ADULT_AGE;
    }

    private int calculateDiscountFare(final int fare, final double teenagerDiscount) {
        return (int) ((fare - DEDUCTION) * teenagerDiscount + DEDUCTION);
    }
}
