package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class FareTest {

    @DisplayName("나잇값이 음수일 때 에러가 발생한다.")
    @Test
    void ageNegativeNumberException() {
        //given
        Fare fare = new Fare(10, -1);
        //when, then
        assertThatThrownBy(fare::calculate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 나이로 요금을 계산할 수 없습니다.");
    }

    @DisplayName("나이가 성인일 때 할인율 없음")
    @Nested
    class FareAdultTest {

        @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
        @Test
        void extractFareUnder10Km() {
            //given
            Fare fare = new Fare(10, 20);

            //when
            int result = fare.calculate();

            //then
            assertThat(result).isEqualTo(1_250);
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
        void extractFareOver10KmUnder50Km(int multiply) {
            //given
            int distance = 10 + (5 * multiply);
            Fare fare = new Fare(distance, 20);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 100 * multiply;
            assertThat(result).isEqualTo(1_250 + extraFare);
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (50km 초과)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void extractFareOver50Km(int multiply) {
            //given
            int distance = 50 + (8 * multiply);
            Fare fare = new Fare(distance, 20);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 800 + (100 * multiply);
            assertThat(result).isEqualTo(1_250 + extraFare);
        }
    }

    @DisplayName("나이가 어린이 일 때 (350 원 공제 후 50% 할인)")
    @Nested
    class FareChildrenTest {
        @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
        @Test
        void extractFareUnder10Km() {
            //given
            Fare fare = new Fare(10, 6);

            //when
            int result = fare.calculate();

            //then
            assertThat(result).isEqualTo((int) ((1_250 - 350) * 0.5));
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
        void extractFareOver10KmUnder50Km(int multiply) {
            //given
            int distance = 10 + (5 * multiply);
            Fare fare = new Fare(distance, 6);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 100 * multiply;
            assertThat(result).isEqualTo((int) ((1_250 + extraFare - 350) * 0.5));
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (50km 초과)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void extractFareOver50Km(int multiply) {
            //given
            int distance = 50 + (8 * multiply);
            Fare fare = new Fare(distance, 6);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 800 + (100 * multiply);
            assertThat(result).isEqualTo((int) ((1_250 + extraFare - 350) * 0.5));
        }
    }

    @DisplayName("나이가 청소년 일 때 (350 원 공제 후 20% 할인)")
    @Nested
    class FareTeenagerTest {
        @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
        @Test
        void extractFareUnder10Km() {
            //given
            Fare fare = new Fare(10, 13);

            //when
            int result = fare.calculate();

            //then
            assertThat(result).isEqualTo((int) ((1_250 - 350) * 0.8));
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
        void extractFareOver10KmUnder50Km(int multiply) {
            //given
            int distance = 10 + (5 * multiply);
            Fare fare = new Fare(distance, 13);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 100 * multiply;
            assertThat(result).isEqualTo((int) ((1_250 + extraFare - 350) * 0.8));
        }

        @DisplayName("경로의 요금을 계산할 수 있다. (50km 초과)")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void extractFareOver50Km(int multiply) {
            //given
            int distance = 50 + (8 * multiply);
            Fare fare = new Fare(distance, 13);

            //when
            int result = fare.calculate();

            //then
            int extraFare = 800 + (100 * multiply);
            assertThat(result).isEqualTo((int) ((1_250 + extraFare - 350) * 0.8));
        }
    }


}