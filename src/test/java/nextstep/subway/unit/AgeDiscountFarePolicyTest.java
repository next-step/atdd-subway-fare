package nextstep.subway.unit;

import nextstep.subway.domain.fare.discount.DiscountFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("운임 할인")
public class AgeDiscountFarePolicyTest {

    @DisplayName("어린이(6세 이상~ 13세 미만) 운임에서 350원을 공제한 금액의 50%할인")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void 어린이_요금_할인(int age) {
        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(1050, age);
        assertThat(discountFarePolicy.discount()).isEqualTo(350);
    }

    @DisplayName("청소년(13세 이상~19세 미만) 운임에서 350원을 공제한 금액의 20%할인")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void 청소년_요금_할인(int age) {
        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(1050, age);
        assertThat(discountFarePolicy.discount()).isEqualTo(560);
    }

    @DisplayName("성인 운임 할인 없음")
    @ParameterizedTest
    @ValueSource(ints = {19, 30})
    void 성인_요금_할인_없음(int age) {
        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(1050, age);
        assertThat(discountFarePolicy.discount()).isEqualTo(1050);
    }

}
