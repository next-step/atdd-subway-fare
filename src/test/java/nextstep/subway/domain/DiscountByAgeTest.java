package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountByAgeTest {


    @ParameterizedTest
    @CsvSource(value = {"0,1250,0", "1,1250,0", "5,1250,0", "6,1250,800", "12,1250,800", "13,1250,1070", "18,1250,1070", "19,1250,1250"})
    void get(int age, int fare, int expectedDiscountFare) {

        // when
        int resultFare = new DiscountByAge(age, fare).getDiscountedFare();

        // then
        assertThat(resultFare).isEqualTo(expectedDiscountFare);
    }
}