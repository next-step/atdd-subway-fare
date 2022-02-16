package nextstep.subway.unit.farepolicy.discount;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.farepolicy.discountcondition.KidsFareDiscountCondition;

@DisplayName("연령별 할인 정책 - 키즈")
public class KidsFareDiscountConditionTest {
    /**
     * 6세 이상 ~ 13세 미만 - 350원을 공제 후 50%를 할인
     * */
    @CsvSource({
        "6,10000,4825",
        "12,10000,4825",
    })
    @DisplayName("6세 이상 ~ 13세 미만의 어린이 할인")
    @ParameterizedTest
    void discountForChild(int age, int totalCost, int expected) {
        FareDiscountCondition fareDiscountPolicy = new KidsFareDiscountCondition(age);
        assertThat(fareDiscountPolicy.discount(totalCost)).isEqualTo(expected);
    }

    /**
     * 13세 이상 ~ 19세 미만 - 350원을 공제한 금액에서 20%를 할인한다.
     * */
    @CsvSource({
        "13,10000,7720",
        "18,10000,7720"
    })
    @DisplayName("13세 이상 ~ 19세 미만의 청소년 할인")
    @ParameterizedTest
    void discountForTeenager(int age, int totalCost, int expected) {
        FareDiscountCondition fareDiscountPolicy = new KidsFareDiscountCondition(age);
        assertThat(fareDiscountPolicy.discount(totalCost)).isEqualTo(expected);
    }

    /**
     * 19세 이상 또는 5세 이하 - 할인하지 않는다.
     * */
    @CsvSource({
        "19,10000,10000"
    })
    @DisplayName("19세 이상의 성인과 5세 이하의 어린이는 할인하지 않는다.")
    @ParameterizedTest
    void noneDiscount(int age, int totalCost, int expected) {
        FareDiscountCondition fareDiscountPolicy = new KidsFareDiscountCondition(age);
        assertThat(fareDiscountPolicy.discount(totalCost)).isEqualTo(expected);
    }
}
