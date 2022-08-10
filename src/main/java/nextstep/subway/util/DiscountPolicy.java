package nextstep.subway.util;

@FunctionalInterface
public interface DiscountPolicy {

    int discount(int fare);
}
