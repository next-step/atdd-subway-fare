package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("어린이 요금은 요금에서 350원을 공제하고 50% 할인을 적용한다.")
    @Test
    void getChildrenFare() {
        assertThat(Age.CHILDREN.discountFare(1350)).isEqualTo(500);
    }

    @DisplayName("청소년 요금은 요금에서 350원을 공제하고 20% 할인을 적용한다.")
    @Test
    void getTeenagerFare() {
        assertThat(Age.TEENAGER.discountFare(1350)).isEqualTo(800);
    }

    @DisplayName("성인 요금은 별도의 할인은 존재하지 않는다.")
    @Test
    void getAdultFare() {
        assertThat(Age.ADULT.discountFare(1350)).isEqualTo(1350);
    }
}
