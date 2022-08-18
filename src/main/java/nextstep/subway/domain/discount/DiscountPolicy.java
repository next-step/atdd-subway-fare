package nextstep.subway.domain.discount;

public interface DiscountPolicy {
    boolean support(int age);
    int discount(int fare);
}
