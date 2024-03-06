package nextstep.path.domain;

public interface DiscountCondition {

    boolean support();

    int discount(int originFare);
}
