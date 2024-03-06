package nextstep.path;

import nextstep.path.domain.Fare;
import nextstep.path.fixture.FareSteps;
import nextstep.subway.fixture.PathSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최소경로 기준 요금 계산")
class DistanceFareTest {

    @DisplayName("9km 요금은")
    @Test
    void DistanceFareTest_case0() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare();

        // when
        int sum = 0;
        for (DistanceFare sut : distanceFare) {
            sum += sut.calculateFare(FareSteps.buildFare(9));
        }

        // then
        assertThat(sum).isEqualTo(1250);
    }


    @DisplayName("12km 요금은")
    @Test
    void DistanceFareTest_case1() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare();

        // when
        int sum = 0;
        for (DistanceFare sut : distanceFare) {
            sum += sut.calculateFare(FareSteps.buildFare(9));
        }

        // then
        assertThat(sum).isEqualTo(1350);
    }


    @DisplayName("16km 요금은")
    @Test
    void DistanceFareTest() {

        // given
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare();

        // when
        int sum = 0;
        for (DistanceFare sut : distanceFare) {
            sum += sut.calculateFare(FareSteps.buildFare(9));
        }

        assertThat(sum).isEqualTo(1450);
    }

}