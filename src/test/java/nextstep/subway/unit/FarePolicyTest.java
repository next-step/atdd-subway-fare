package nextstep.subway.unit;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.discount.DiscountCalculator;
import nextstep.subway.domain.fare.BasicFarePolicy;
import nextstep.subway.domain.fare.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;

import static org.assertj.core.api.Assertions.assertThat;

public class FarePolicyTest {

    @DisplayName("요금 계산 - 10km 이하")
    @Test
    void calculateOverFareFromBasic() {

        // given
        final int distance = 10;

        // when
        BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
        long fare = basicFarePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("요금 계산 - 10km 초과 50km 이하")
    @Test
    void calculateOverFareFrom10KM() {

        // given
        final int distance = 16;

        // when
        BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
        long fare = basicFarePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("요금 계산 - 50km 이상")
    @Test
    void calculateOverFareFrom50KM() {
        // given
        final int distance = 59;

        // when
        BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
        long fare = basicFarePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(2250);
    }

    @DisplayName("요금 계산 - 어린이 할인 정책 적용")
    @Test
    void calculateOverFareApplyDiscountChildren() {
        // given
        final int distance = 10;
        BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
        long fare = basicFarePolicy.calculateOverFare(distance);

        // when
        long discountFare = DiscountCalculator.applyToDiscountFare(DiscountCalculator.DiscountPolicy.CHILDREN, fare);

        // then
        assertThat(discountFare).isEqualTo(800);

    }

    @DisplayName("요금 계산 - 청소년 할인 정책 적용")
    @Test
    void calculateOverFareApplyDiscountTeenager() {

        // given
        final int distance = 10;
        BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
        long fare = basicFarePolicy.calculateOverFare(distance);

        // when
        long discountFare = DiscountCalculator.applyToDiscountFare(DiscountCalculator.DiscountPolicy.TEENAGER, fare);

        // then
        assertThat(discountFare).isEqualTo(1070);
    }
}
