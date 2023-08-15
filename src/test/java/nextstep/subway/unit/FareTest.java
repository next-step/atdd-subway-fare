package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.Test;

class FareTest {

    private final Fare fare = new Fare();

    @Test
    void getFareByDistance() {
        int expectedFare = fare.getFareByDistance(10);
        int expectedFare1 = fare.getFareByDistance(12);
        int expectedFare2 = fare.getFareByDistance(16);


        assertThat(expectedFare).isEqualTo(1250);
        assertThat(expectedFare1).isEqualTo(1350);
        assertThat(expectedFare2).isEqualTo(1450);
    }
}
