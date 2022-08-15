package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FareTest {

    @DisplayName("기본 요금")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "6:1250"}, delimiter = ':')
    void basicFare(int distance, int expectedFare) {
        // given

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("10km 이상 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "20:1450"}, delimiter = ':')
    void overTenKilo(int distance, int expectedFare) {
        // given

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("50km 이상 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150"}, delimiter = ':')
    void overFiftyKilo(int distance, int expectedFare) {
        // given

        // when
        int fare = Fare.getFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @Test
    @DisplayName("잘못된 거리 입력")
    void invalidDistance() {
        // given
        int distance = -1;

        // when
        Executable executable = () -> Fare.getFare(distance);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

}
