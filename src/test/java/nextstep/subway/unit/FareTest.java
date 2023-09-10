package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.fare.DefaultFareChain;
import nextstep.subway.domain.fare.FareChain;
import nextstep.subway.domain.fare.KidFareChain;
import nextstep.subway.domain.fare.LongRangeFareChain;
import nextstep.subway.domain.fare.MidRangeFareChain;
import nextstep.subway.domain.fare.TeenFareChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FareTest {

    private final FareChain fare = new DefaultFareChain();
    private final FareChain midFare = new MidRangeFareChain();
    private final FareChain longFare = new LongRangeFareChain();
    private final FareChain kidFare = new KidFareChain();
    private final FareChain teenFare = new TeenFareChain();

    @BeforeEach
    void init() {
        midFare.setNextChain(longFare);
        fare.setNextChain(midFare);
        teenFare.setNextChain(fare);
        kidFare.setNextChain(teenFare);
    }

    @Test
    void getFareByKid() {
        int fare = kidFare.calculateFare(5, 300, 6);

        assertThat(fare).isEqualTo(950);
    }

    @Test
    void getFareByTeen() {
        int fare = kidFare.calculateFare(5, 300, 18);

        assertThat(fare).isEqualTo(1310);
    }

    @Test
    void getFareByDistanceUnderMidRange() {
        int expectedFare = fare.calculateFare(10, 0, 20);
        int expectedFare1 = fare.calculateFare(12, 0, 20);
        int expectedFare2 = fare.calculateFare(16, 0, 20);


        assertThat(expectedFare).isEqualTo(1250);
        assertThat(expectedFare1).isEqualTo(1350);
        assertThat(expectedFare2).isEqualTo(1450);
    }

    @Test
    void getFareByDistanceOverMidRange() {
        int expectedFare = fare.calculateFare(51, 0, 20);
        int expectedFare1 = fare.calculateFare(50, 0, 20);
        int expectedFare2 = fare.calculateFare(75, 0, 20);

        assertThat(expectedFare).isEqualTo(2150);
        assertThat(expectedFare1).isEqualTo(2050);
        assertThat(expectedFare2).isEqualTo(2450);
    }
}
