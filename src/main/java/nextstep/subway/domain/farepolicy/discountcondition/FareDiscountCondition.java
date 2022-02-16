package nextstep.subway.domain.farepolicy.discountcondition;

public interface FareDiscountCondition {
    int discount(int totalCost);
}
