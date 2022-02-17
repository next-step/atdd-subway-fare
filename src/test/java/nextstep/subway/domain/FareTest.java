package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class FareTest {

    @Test
    void calculateFare() {
        //given
        int distance = 100;
        int age = 20;
        Line 일호선 = new Line("일호선", "", 500);
        Line 이호선 = new Line("이호선", "", 100);
        Line 삼호선 = new Line("삼호선", "", 0);

        //when
        int result = new Fare(distance, age, true, Arrays.asList(일호선, 이호선, 삼호선)).calculateFare();

        //then
        assertThat(result).isEqualTo(3_250);
    }
}