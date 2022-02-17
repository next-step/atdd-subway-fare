package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class FareTest {

    @Test
    void calculateFare() {
        //given
        int distance = 100;
        int age = 20;
        List<String> lineNames = Arrays.asList("일호선", "이호선", "삼호선");

        //when
        int result = new Fare(distance, age, true , lineNames).calculateFare();

        //then
        assertThat(result).isEqualTo(3_250);
    }
}