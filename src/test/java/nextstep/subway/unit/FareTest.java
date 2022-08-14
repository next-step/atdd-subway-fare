package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    @DisplayName("기본 요금")
    void basicFare() {
        // given
        int distance = 10;
        int expectedFare = 1250;

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @Test
    @DisplayName("10km 이상 추가요금")
    void overTenKilo() {
        // given
        int distance = 11;
        int expectedFare = 1350;

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @Test
    @DisplayName("50km 이상 추가요금")
    void overFiftyKilo() {
        // given
        int distance = 51;
        int expectedFare = 1850;

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }


}
