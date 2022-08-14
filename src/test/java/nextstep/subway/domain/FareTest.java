package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;


class FareTest {

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
    @Test
    void extractFareUnder10Km() {
        //given
        Fare fare = new Fare(10);

        //when
        int result = fare.calculate();

        //then
        assertThat(result).isEqualTo(1_250);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void extractFareOver10KmUnder50Km(int multiply) {
        //given
        int distance = 10 + (5 * multiply);
        Fare fare = new Fare(distance);

        //when
        int result = fare.calculate();

        //then
        int extraFare = 100 * multiply;
        assertThat(result).isEqualTo(1_250 + extraFare);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (50km 초과)")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void extractFareOver50Km(int multiply) {
        //given
        int distance = 50 + (8 * multiply);
        Fare fare = new Fare(distance);

        //when
        int result = fare.calculate();

        //then
        int extraFare = 800 + (100 * multiply);
        assertThat(result).isEqualTo(1_250 + extraFare);
    }

}