package nextstep.subway.unit;

import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @ParameterizedTest
    @CsvSource({
            "10, 5, 0",
            "10, 6, 850",
            "10, 13, 1150",
            "10, 20, 1350",

            "20, 5, 0",
            "20, 6, 950",
            "20, 13, 1310",
            "20, 20, 1550",

            "60, 5, 0",
            "60, 6, 1200",
            "60, 13, 1710",
            "60, 20, 2050"
    })
    void impose(int totalDistance, int memberAge, int expectedFare) {
        // given
        int maxLineFare = 100;
        Fare fare = new Fare();

        // when
        fare.impose(totalDistance, memberAge, maxLineFare);

        // then
        assertThat(fare.getTotalFare()).isEqualTo(expectedFare);
    }
}
