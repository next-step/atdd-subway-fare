package nextstep.subway.domain;

import nextstep.subway.domain.fare.AgeFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {


    @DisplayName("나이에 따른 할인 요금을 확인")
    @ParameterizedTest(name = "[{index}] 나이가 {0}세이고 요금이 {1}원인 경우, 할인 된 요금은 {2}원이다.")
    @CsvSource(value = {"0,1250,0", "1,1250,0", "5,1250,0", "6,1250,800", "12,1250,800", "13,1250,1070", "18,1250,1070", "19,1250,1250"})
    void get(int age, int fare, int expectedDiscountFare) {

        // when
        int resultFare = new AgeFarePolicy(Optional.of(age)).fare(fare);

        // then
        assertThat(resultFare).isEqualTo(expectedDiscountFare);
    }
}