package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {
    @DisplayName("나이를 입력으로 받으면 그에 맞는 enum값을 리턴한다.")
    @ParameterizedTest
    @CsvSource({
        "6,CHILDREN",
        "12,CHILDREN",
        "13,TEENAGER",
        "18,TEENAGER",
        "19,ADULT",
        "30,ADULT"
    })
    void getAge(int intAge, Age age) {
        assertThat(Age.of(intAge)).isEqualTo(age);
    }

    @DisplayName("입력으로 요금이 주어지면 나이대에 맞는 할인된 값을 리턴한다.")
    @ParameterizedTest
    @CsvSource({
        "CHILDREN,1350,500",
        "TEENAGER,1350,800",
        "ADULT,1350,1350"
    })
    void getDiscountFare(Age age, int fare, int expected) {
        assertThat(age.discountFare(fare)).isEqualTo(expected);
    }
}
