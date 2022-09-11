package nextstep.subway.unit;

import nextstep.subway.domain.DiscountFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FareDiscountFarePolicy1Test {

    @DisplayName("어린이: 운임에서 350원을 공제한 금액의 50%할인")
    @Test
    void 어린이_요금_할인() {

        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(7);

        assertAll(
                () -> assertThat(discountFarePolicy.discountRatio()).isEqualTo(0.5),
                () -> assertThat(discountFarePolicy.discountFare()).isEqualTo(350)
        );

    }

    @DisplayName("운임에서 350원을 공제한 금액의 20%할인")
    @Test
    void 청소년_요금_할인() {

        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(15);

        assertAll(
                () -> assertThat(discountFarePolicy.discountRatio()).isEqualTo(0.8),
                () -> assertThat(discountFarePolicy.discountFare()).isEqualTo(350)
        );

    }

    @DisplayName("성인 운임 할인 없음")
    @Test
    void 성인_요금_할인_없음() {

        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(20);

        assertAll(
                () -> assertThat(discountFarePolicy.discountFare()).isEqualTo(0),
                () -> assertThat(discountFarePolicy.discountRatio()).isEqualTo(1)
        );

    }

}
