package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    void calculateOverFare() {
        Fare fare = new Fare(10);
        assertThat(fare.calculateOverFareBefore50(12)).isEqualTo(100);
        assertThat(fare.calculateOverFareBefore50(16)).isEqualTo(200);

        assertThat(fare.calculateOverFareAfter50(51)).isEqualTo(100);
        assertThat(fare.calculateOverFareAfter50(57)).isEqualTo(100);
        assertThat(fare.calculateOverFareAfter50(59)).isEqualTo(200);

    }
}
