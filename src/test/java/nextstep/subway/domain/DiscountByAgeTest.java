package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountByAgeTest {

    /**
     * 나이 0    1350    2350
     * 나이 1
     * 나이 6
     * 나이 7
     * 나이 12
     * 나이 13
     * 나이 18
     * 나이 19
     */
    @ParameterizedTest
    @CsvSource(value = {"0,1350,0", "1,1350,0", "5,1350,0", "6,1350,850", "12,1350,850", "13,1350,1150", "18,1350,1150", "19,1350,1350"})
    void get(int age, int fare, int expectedDiscountFare) {

        // when
        int resultFare = new DiscountByAge(age, fare).getDiscountedFare();

        // then
        assertThat(resultFare).isEqualTo(expectedDiscountFare);
    }
}