package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.fare.DefaultFareChain;
import nextstep.subway.domain.fare.FareChain;
import nextstep.subway.domain.fare.LongRangeFareChain;
import nextstep.subway.domain.fare.MidRangeFareChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FareTest {

    private final FareChain fare = new DefaultFareChain();
    private final FareChain midFare = new MidRangeFareChain();
    private final FareChain longFare = new LongRangeFareChain();

    @BeforeEach
    void init() {
        midFare.setNextChain(longFare);
        fare.setNextChain(midFare);
    }

    @Test
    void getFareByDistanceUnderMidRange() {
        int expectedFare = fare.calculateFare(10);
        int expectedFare1 = fare.calculateFare(12);
        int expectedFare2 = fare.calculateFare(16);


        assertThat(expectedFare).isEqualTo(1250);
        assertThat(expectedFare1).isEqualTo(1350);
        assertThat(expectedFare2).isEqualTo(1450);
    }

    @Test
    void getFareByDistanceOverMidRange() {
        int expectedFare = fare.calculateFare(51);
        int expectedFare1 = fare.calculateFare(50);
        int expectedFare2 = fare.calculateFare(75);

        assertThat(expectedFare).isEqualTo(2150);
        assertThat(expectedFare1).isEqualTo(2050);
        assertThat(expectedFare2).isEqualTo(2450);
    }
}
