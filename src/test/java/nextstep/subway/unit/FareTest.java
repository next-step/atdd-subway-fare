package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("10km 이내 거리에 대한 요금 계산")
    @Test
    void calculateFareWithUnder10km() {
        // when
        final int fare = Fare.calculateAmount(10);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10km 거리에 대한 요금 계산")
    @Test
    void calculateFareWithOver10kmAndUpTo50km() {
        // when
        final int fare = Fare.calculateAmount(11);

        // then
        assertThat(fare).isEqualTo(1350);
    }

    @DisplayName("50km 이하 거리에 대한 요금 계산")
    @Test
    void calculateFareWithUpTo50km() {
        // when
        final int fare = Fare.calculateAmount(50);

        // then
        assertThat(fare).isEqualTo(2050);
    }

    @DisplayName("50km 초과 거리에 대한 요금 계산")
    @Test
    void calculateFareWithOver50km() {
        // when
        final int fare = Fare.calculateAmount(51);

        // then
        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("50km 초과 거리에 대한 요금 계산")
    @Test
    void calculateFareWith59Distance() {
        // when
        final int fare = Fare.calculateAmount(59);

        // then
        assertThat(fare).isEqualTo(2250);
    }
}
