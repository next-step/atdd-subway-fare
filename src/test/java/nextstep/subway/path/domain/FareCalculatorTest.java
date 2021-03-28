package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static nextstep.subway.path.application.PathService.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DisplayName("운임 요금 계산기 테스트")
public class FareCalculatorTest {

    private final AdditionalFareCalculator additionalFareCalculator = new AdditionalFareCalculator(0);

    @DisplayName("이용 거리초과 시 추가운임 부과")
    @Nested
    @TestInstance(PER_CLASS)
    class 초과_요금_계산 {

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원)")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver10FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(additionalFareCalculator.calculate(distance));
        }

        @DisplayName("50km초과 시 (8km마다 100원)")
        @MethodSource("over50Fares")
        @ParameterizedTest
        void calculateOver50FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(additionalFareCalculator.calculate(distance));
        }

        Stream<Arguments> over10Fares() {
            return provideDistanceOver10AndFare();
        }

        Stream<Arguments> over50Fares() {
            return provideDistanceOver50AndFare();
        }
    }

    @DisplayName("노선별 추가 요금 계산")
    @Nested
    class 노선별_초과_요금_계산 {

        @DisplayName("단일 노선에 대한 추가요금 테스트")
        @Test
        void singleLineAdditionalFareTest() {
            int additionalFare = 100;
            AdditionalFareCalculator additionalFareCalculator = new AdditionalFareCalculator(
                Collections.singletonList(new Line("분당선", "yellow", additionalFare))
            );

            assertThat(additionalFare)
                .isEqualTo(additionalFareCalculator.calculate(5));
        }

        @DisplayName("여러 노선에 대한 추가요금 테스트")
        @Test
        void multiLineAdditionalFareTest() {
            int maxAdditionalFare = 900;
            AdditionalFareCalculator additionalFareCalculator = new AdditionalFareCalculator(
                Arrays.asList(
                    new Line("분당선", "yellow", 100),
                    new Line("삼호선", "orage", 200),
                    new Line("신분당선", "red", maxAdditionalFare)
                )
            );

            assertThat(maxAdditionalFare)
                .isEqualTo(additionalFareCalculator.calculate(5));
        }
    }

    @DisplayName("연령에 따른 요금 할인 반영")
    @TestInstance(PER_CLASS)
    @Nested
    class 요금_할인 {

        @DisplayName("어린이 요금 할인")
        @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
        @ParameterizedTest
        void discountOfChildTest(int age) {
            LoginMember loginMember = new LoginMember(1L, "email@email.com", "1234", age);
            DiscountFareCalculator discountFareCalculator = new DiscountFareCalculator(loginMember);

            assertThat((DEFAULT_FARE -  350) * 0.5)
                .isEqualTo(discountFareCalculator.calculate(DEFAULT_FARE));
        }

        @DisplayName("청소년 요금 할인")
        @ValueSource(ints = {13, 14, 15, 16, 17, 18})
        @ParameterizedTest
        void discountOfYouthTest(int age) {
            LoginMember loginMember = new LoginMember(1L, "email@email.com", "1234", age);
            DiscountFareCalculator discountFareCalculator = new DiscountFareCalculator(loginMember);

            assertThat((DEFAULT_FARE -  350) * 0.2)
                .isEqualTo(discountFareCalculator.calculate(DEFAULT_FARE));
        }

    }

    public static Stream<Arguments> provideDistanceOver10AndFare() {
        return Stream.of(
            Arguments.of(15, 100),
            Arguments.of(20, 200),
            Arguments.of(25, 300),
            Arguments.of(30, 400),
            Arguments.of(35, 500),
            Arguments.of(40, 600),
            Arguments.of(45, 700),
            Arguments.of(50, 800)
        );
    }

    public static Stream<Arguments> provideDistanceOver50AndFare() {
        return Stream.of(
            Arguments.of(58, 900),
            Arguments.of(66, 1000),
            Arguments.of(72, 1100),
            Arguments.of(80, 1200)
        );
    }
}
