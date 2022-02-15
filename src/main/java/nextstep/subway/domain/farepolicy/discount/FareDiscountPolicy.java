package nextstep.subway.domain.farepolicy.discount;

public interface FareDiscountPolicy {
    int discount(int totalCost);
}
