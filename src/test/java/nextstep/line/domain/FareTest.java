package nextstep.line.domain;

import nextstep.exception.FareNotMatchException;
import nextstep.line.domain.fare.DiscountFarePolicies;
import nextstep.line.domain.fare.DistanceFarePolicies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.member.MemberTestUser.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    private final DistanceFarePolicies distanceFarePolicies = new DistanceFarePolicies();
    private final DiscountFarePolicies discountFarePolicies = new DiscountFarePolicies();

    @DisplayName("이동거리에 따라 알맞는 요금이 산출되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"3,1250","9,1250","12,1350","16,1450","52,2150"}, delimiterString = ",")
    void 요금산출(int distance, int fare) {
        // when then
        assertThat(distanceFarePolicies.getFare(distance)).isEqualTo(fare);
    }

    @DisplayName("이동거리가 비정상일 경우 에러가 발생한다.")
    @Test
    void 요금산출_비정상거리() {
        // given
        int distance = -1;
        // when then
        assertThatThrownBy(() -> distanceFarePolicies.getFare(distance))
                .isExactlyInstanceOf(FareNotMatchException.class)
                .hasMessage("요금 산출에 실패했습니다.");
    }

    @DisplayName("청소년에 경우 350을 공제한 금액의 20% 할인이 적용되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"10000,7720","5000,3720"}, delimiterString = ",")
    void 청소년할인요금(int fare, int discountFare) {
        assertThat(discountFarePolicies.getDiscountFare(fare, 청소년)).isEqualTo(discountFare);
    }

    @DisplayName("어린이에 경우 350을 공제한 금액의 50% 할인이 적용되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"10000,4825","5000,2325"}, delimiterString = ",")
    void 어린이할인요금(int fare, int discountFare) {
        assertThat(discountFarePolicies.getDiscountFare(fare, 어린이)).isEqualTo(discountFare);
    }

    @DisplayName("성인은 할인이 적용되지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"10000,10000","5000,5000"}, delimiterString = ",")
    void 성인할인요금(int fare, int discountFare) {
        assertThat(discountFarePolicies.getDiscountFare(fare, 성인)).isEqualTo(discountFare);
    }

    @DisplayName("비회원은 할인이 적용되지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"10000,10000","5000,5000"}, delimiterString = ",")
    void 비회원할인요금(int fare, int discountFare) {
        assertThat(discountFarePolicies.getDiscountFare(fare, 비회원)).isEqualTo(discountFare);
    }
}
