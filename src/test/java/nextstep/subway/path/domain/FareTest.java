package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @ParameterizedTest
    @DisplayName("10km 이내일 경우 기본 요금")
    @CsvSource({"7, 1250", "8, 1250", "10, 1250"})
    void NotOverFare10km(int distance, int basicFare) {
        Fare fare = Fare.of(distance);
        assertThat(fare.getFare()).isEqualTo(basicFare);
    }

    @ParameterizedTest
    @DisplayName("11km ~ 50km 이내일 경우 5Km 당 100원 추가요금 발생한다")
    @CsvSource({"13, 1250", "24, 1450"})
    void overFare11kmAnd50km(int distance, int addFare) {
        Fare fare = Fare.of(distance);
        assertThat(fare.getFare()).isEqualTo(addFare);
    }

    @ParameterizedTest
    @DisplayName("거리가 50km 초과될 경우 8Km 당 100원 추가요금 발생한다")
    @CsvSource({"58, 2150", "60, 2250"})
    void overFare50(int distance, int addFare) {
        Fare fare = Fare.of(distance);
        assertThat(fare.getFare()).isEqualTo(addFare);
    }
}
