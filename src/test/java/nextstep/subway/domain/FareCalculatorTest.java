package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {

    @DisplayName("거리가 50km이하시 요금 5km 마다 100원 추가한다")
    @ParameterizedTest
    @CsvSource({
            "50,1000",
            "1,100",
            "6,200",
            "10,200"
    })
    void shouldAdd100WonForEach5KmWhenDistanceIsLessThanOrEqualTo50Km(int distance,int expect) {
        // when
        int fare = FareCalculator.calculateByDistance(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }
}
