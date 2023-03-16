package nextstep.subway.unit.fare;

import nextstep.subway.domain.fare.AgeFareType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgeFareTypeTest {
    @DisplayName("나이가 음수일 경우 예외가 발생한다.")
    @Test
    void exceptionMinusAge() {
        assertThatThrownBy(() -> AgeFareType.findByAge(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이가 음수일 수 없습니다.");
    }

    @DisplayName("유아이거나, 노인일 경우 요금은 0원이다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1250, 0",
            "1550, 0",
            "1850, 0",
            "2150, 0"
    })
    void babyAndElderFare(int totalFare, int expectedFare) {
        // given
        int babyAge = 4;
        int elderAge = 70;
        AgeFareType baby = AgeFareType.findByAge(babyAge);
        AgeFareType elder = AgeFareType.findByAge(elderAge);

        // when
        int babyActualFare = totalFare - baby.discountFare(totalFare);
        int elderActualFare = totalFare - elder.discountFare(totalFare);

        // then
        assertAll(
                () -> assertThat(babyActualFare).isEqualTo(expectedFare),
                () -> assertThat(elderActualFare).isEqualTo(expectedFare)
        );
    }

    @DisplayName("어린이일 경우 요금은 350원 공제된 금액에서 50% 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1250, 450",
            "1550, 600",
            "1850, 750",
            "2150, 900"
    })
    void childFare(int totalFare, int expectedFare) {
        // given
        int childAge = 8;
        AgeFareType child = AgeFareType.findByAge(childAge);

        // when
        int childActualFare = totalFare - child.discountFare(totalFare);

        // then
        assertThat(childActualFare).isEqualTo(expectedFare);
    }

    @DisplayName("청소년일 경우 요금은 350원 공제된 금액에서 20% 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1250, 720",
            "1550, 960",
            "1850, 1200",
            "2150, 1440"
    })
    void youthFare(int totalFare, int expectedFare) {
        // given
        int youthAge = 16;
        AgeFareType youth = AgeFareType.findByAge(youthAge);

        // when
        int youthActualFare = totalFare - youth.discountFare(totalFare);

        // then
        assertThat(youthActualFare).isEqualTo(expectedFare);
    }

    @DisplayName("성인일 경우 요금은 할인되지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1250, 1250",
            "1550, 1550",
            "1850, 1850",
            "2150, 2150"
    })
    void adultFare(int totalFare, int expectedFare) {
        // given
        int adultAge = 29;
        AgeFareType adult = AgeFareType.findByAge(adultAge);

        // when
        int adultActualFare = totalFare - adult.discountFare(totalFare);

        // then
        assertThat(adultActualFare).isEqualTo(expectedFare);
    }
}
