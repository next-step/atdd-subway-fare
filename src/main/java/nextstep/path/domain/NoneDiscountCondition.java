package nextstep.path.domain;

public class NoneDiscountCondition implements DiscountCondition {
    @Override
    public boolean support() {
        return false;
    }

    @Override
    public int discount(int originFare) {
        return originFare;
    }
}
