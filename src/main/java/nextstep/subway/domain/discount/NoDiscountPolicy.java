package nextstep.subway.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class NoDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean support(int age) {
        return true;
    }

    @Override
    public int discount(int fare) {
        return fare;
    }

}
