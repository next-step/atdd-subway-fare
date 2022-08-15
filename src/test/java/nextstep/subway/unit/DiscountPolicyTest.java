package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.discount.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiscountPolicyTest {

    private static final Integer BASIC_PRICE = 1250;

    @DisplayName("6세 이상 13미만 나이 할인 금액 테스트")
    @ParameterizedTest(name = "{displayName} - 나이 : {0}")
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12 })
    public void childDiscount(int age) {
        int price = DiscountPolicy.discount(BASIC_PRICE, age);

        assertThat(price).isEqualTo(450);
    }

    @DisplayName("13세 이상 19세 미만 나이 할인 금액 테스트")
    @ParameterizedTest(name = "{displayName} - 나이 : {0}")
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    public void teenagerDiscountTest(int age) {
        int price = DiscountPolicy.discount(BASIC_PRICE, age);

        assertThat(price).isEqualTo(720);
    }

    @DisplayName("19세 이상 할인 안 된 할인 금액 테스트")
    @ParameterizedTest(name = "{displayName} - 나이 : {0}")
    @ValueSource(ints = {0, 1, 2,3 ,4, 5, 19, 20, 21})
    public void notDiscountTest(int age) {
        int price = DiscountPolicy.discount(BASIC_PRICE, age);

        assertThat(price).isEqualTo(BASIC_PRICE);
    }

}
