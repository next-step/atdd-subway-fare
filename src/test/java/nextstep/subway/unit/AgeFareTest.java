package nextstep.subway.unit;

import nextstep.subway.domain.AgeFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class AgeFareTest {

    @DisplayName("어린이 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    void findChild(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.CHILD_FARE);
    }

    @DisplayName("청소년 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 16, 18})
    void findYouth(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.YOUTH_FARE);
    }

    @DisplayName("일반 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 25, 99})
    void findGeneral(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.GENERAL_FARE);
    }

    @DisplayName("어린이는 공제액 350원을 제외한 50퍼를 할인 받는다.")
    @ParameterizedTest
    @CsvSource(value = {"1350:850", "1450:900"}, delimiter = ':')
    void discountChild(BigDecimal fare, BigDecimal discountFare) {
        // when
        BigDecimal result = AgeFare.CHILD_FARE.extractDiscountFare(fare);

        // then
        assertThat(result.compareTo(discountFare)).isEqualTo(0);
    }

    @DisplayName("청소년은 공제액 350원을 제외한 20퍼를 할인 받는다.")
    @ParameterizedTest
    @CsvSource(value = {"1350:1150", "1450:1230"}, delimiter = ':')
    void discountYouh(BigDecimal fare, BigDecimal discountFare) {
        // when
        BigDecimal result = AgeFare.YOUTH_FARE.extractDiscountFare(fare);

        // then
        assertThat(result.compareTo(discountFare)).isEqualTo(0);
    }

    @DisplayName("일반인은 할인이 없다.")
    @ParameterizedTest
    @CsvSource(value = {"1350:1350", "1450:1450"}, delimiter = ':')
    void noneDiscount(BigDecimal fare, BigDecimal discountFare) {
        // when
        BigDecimal result = AgeFare.GENERAL_FARE.extractDiscountFare(fare);

        // then
        assertThat(result.compareTo(discountFare)).isEqualTo(0);
    }
}
