package nextstep.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최소경로 기준 요금 계산")
class DistanceFareTest {

    @DisplayName("9km 요금은")
    @Test
    void DistanceFareTest_case0() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare(9);

        // when
        int sum = 0;
        for (DistanceFare fare : distanceFare) {
            sum += fare.calculateFare();
        }

        // then
        assertThat(sum).isEqualTo(1250);
    }


    @DisplayName("12km 요금은")
    @Test
    void DistanceFareTest_case1() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare(12);

        // when
        int sum = 0;
        for (DistanceFare fare : distanceFare) {
            sum += fare.calculateFare();
        }

        // then
        assertThat(sum).isEqualTo(1350);
    }


    @DisplayName("16km 요금은")
    @Test
    void DistanceFareTest() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare(16);

        // when
        int sum = 0;
        for (DistanceFare fare : distanceFare) {
            sum += fare.calculateFare();
        }

        assertThat(sum).isEqualTo(1450);
    }
}