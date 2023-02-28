package nextstep.subway.domain.policy;

public interface FareDiscountPolicy {

    boolean isSuite(CalculateConditions conditions);
    int calculate(int fare);

}
