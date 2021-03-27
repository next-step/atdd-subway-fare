package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceOfTypeTest {

    @DisplayName("원하는 추가요금이 나온다")
    @Test
    void distanceType() {

        // given Basic Fare
        DistanceOfFareType basicDistanceOfFareType = DistanceOfFareType.valueOf(10);

        // then
        assertThat(basicDistanceOfFareType.name()).isEqualTo(DistanceOfFareType.BASIC_FARE.toString());
        assertThat(basicDistanceOfFareType.calculate(10)).isEqualTo(0);

        // given TEN_KM_OVER_FARE
        DistanceOfFareType tenKmOverFare = DistanceOfFareType.valueOf(20);

        // then
        assertThat(tenKmOverFare.name()).isEqualTo(DistanceOfFareType.TEN_KM_OVER_FARE.toString());
        assertThat(tenKmOverFare.calculate(20)).isEqualTo(200);

        // given FIFTY_KM_OVER_FARE
        DistanceOfFareType fiftyKmOverFare = DistanceOfFareType.valueOf(60);

        // then
        assertThat(fiftyKmOverFare.name()).isEqualTo(DistanceOfFareType.FIFTY_KM_OVER_FARE.toString());
        assertThat(fiftyKmOverFare.calculate(60)).isEqualTo(1000);
    }
}
