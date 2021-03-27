package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    @ParameterizedTest
    @DisplayName("요금 거리별 계산")
    @CsvSource(value = {
            "20,10,0,1250",
            "20,20,0,1450",
            "20,58,0,2150"
    })
    void calculate(int age, int distance, int overFare, int fare) {
        Fare actual = new Fare(distance, age, overFare);
        assertThat(actual.getFare()).isEqualTo(fare);
    }

    @ParameterizedTest
    @DisplayName("요금 나이별 계산")
    @CsvSource(value = {
            "6,10,0,450",
            "13,10,0,720",
            "20,10,0,1250"
    })
    void calculateAge(int age, int distance, int overFare, int fare) {
        Fare actual = new Fare(distance, age, overFare);
        assertThat(actual.getFare()).isEqualTo(fare);
    }

    @ParameterizedTest
    @DisplayName("요금 추가요금별 계산")
    @CsvSource(value = {
            "20,10,0,1250",
            "20,10,100,1350",
            "20,10,1000,2250"
    })
    void calculateOverFare(int age, int distance, int overFare, int fare) {
        Fare actual = new Fare(distance, age, overFare);
        assertThat(actual.getFare()).isEqualTo(fare);
    }

    @DisplayName("거리가 0보다 작은 경우")
    @Test
    void validateDistanceLessThanZero() {
        assertThatThrownBy(() -> {new Fare(0, 20, 0); })
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("나이가 0보다 작은 경우")
    @Test
    void validateAgeLessThanZero() {
        assertThatThrownBy(() -> {new Fare(10, 0, 0); })
                .isInstanceOf(IllegalArgumentException.class);
    }

}
