package nextstep.subway.unit;

import nextstep.subway.domain.fare.BasicFarePolicy;
import nextstep.subway.domain.fare.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FarePolicyTest {

    @DisplayName("요금 계산 - 10km 이하")
    @Test
    void calculateOverFareFromBasic() {

        // given
        final int distance = 10;

        // when
        FarePolicy farePolicy = new BasicFarePolicy();
        long fare = farePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("요금 계산 - 10km 초과 50km 이하")
    @Test
    void calculateOverFareFrom10KM() {

        // given
        final int distance = 16;

        // when
        FarePolicy farePolicy = new BasicFarePolicy();
        long fare = farePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("요금 계산 - 50km 이상")
    @Test
    void calculateOverFareFrom50KM() {
        // 51 -> 1250 + 800 + 100 = 2150

        // given
        final int distance = 59;

        // when
        FarePolicy farePolicy = new BasicFarePolicy();
        long fare = farePolicy.calculateOverFare(distance);

        // then
        assertThat(fare).isEqualTo(2250);
    }
}
