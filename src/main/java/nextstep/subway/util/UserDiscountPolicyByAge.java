package nextstep.subway.util;

public class UserDiscountPolicyByAge implements DiscountPolicy {

    private Age age;

    public UserDiscountPolicyByAge(Age age) {
        this.age = age;
    }

    @Override
    public int discount(int fare) {
        return age.discount(fare);
    }
}
