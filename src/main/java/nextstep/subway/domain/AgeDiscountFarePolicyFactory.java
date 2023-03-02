package nextstep.subway.domain;

import nextstep.common.exception.CannotInstanceException;

public class AgeDiscountFarePolicyFactory {

    private AgeDiscountFarePolicyFactory() {
        throw new CannotInstanceException();
    }

    public static FarePolicy getPolicy(int age) {
        if (isTeenager(age)) {
            return new TeenagerDiscountFarePolicy();
        }

        if (isChild(age)) {
            return new ChildDiscountFarePolicy();
        }

        return null;
    }

    private static boolean isTeenager(int age) {
        return age >= 13 && age < 19;
    }

    private static boolean isChild(int age) {
        return age >= 6 & age < 13;
    }
}
