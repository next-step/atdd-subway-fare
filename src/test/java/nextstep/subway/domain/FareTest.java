package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest(name = "최단거리가 {0}일때 {1}원의 요금이 부가된다.")
    @CsvSource(value = {"0,1250", "1,1250", "10,1350", "50,2150", "100,2750"})
    void calculateFare(int distance, int fare) {
        //when
        int result = new Fare(distance).calculateFare();

        //then
        assertThat(result).isEqualTo(fare);
    }
}