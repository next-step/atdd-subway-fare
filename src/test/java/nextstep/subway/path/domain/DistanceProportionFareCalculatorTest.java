package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineFare;
import nextstep.subway.path.application.DistanceProportionFareCalculator;
import nextstep.subway.path.application.FareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DisplayName("운임 요금 계산기 테스트")
public class DistanceProportionFareCalculatorTest {

    private final FareCalculator adultFareCalculator = new DistanceProportionFareCalculator(LineFare.ADULT);

    @DisplayName("기본운임(10㎞ 이내) : 기본운임 1,250원")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ParameterizedTest
    void calculateDefaultFareTest(int distance) {
        assertThat(LineFare.ADULT.getFare())
            .isEqualTo(adultFareCalculator.calculateFare(distance));
    }

    @DisplayName("이용 거리초과 시 추가운임 부과")
    @Nested
    @TestInstance(PER_CLASS)
    class 초과_요금_계산 {

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원)")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver10FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(adultFareCalculator.calculateFare(distance));
        }

        @DisplayName("50km초과 시 (8km마다 100원)")
        @MethodSource("over50Fares")
        @ParameterizedTest
        void calculateOver50FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(adultFareCalculator.calculateFare(distance));
        }

        Stream<Arguments> over10Fares() {
            return provideDistanceOver10AndFare();
        }

        Stream<Arguments> over50Fares() {
            return provideDistanceOver50AndFare();
        }
    }

    @DisplayName("연령에 따른 요금 할인 반영")
    @TestInstance(PER_CLASS)
    @Nested
    class 요금_할인 {

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원) + 청소년 요금 할인")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver10FareTest(int distance, int originFare) {
            FareCalculator youthFareCalculator = new DistanceProportionFareCalculator(LineFare.YOUTH);

            int youthFare = originFare - 350;
            int expected = youthFare - (int)(youthFare * 0.2);
            assertThat(expected)
                .isEqualTo(youthFareCalculator.calculateFare(distance));
        }

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원) + 어린이 요금 할인")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver50FareTest(int distance, int originFare) {
            FareCalculator childFareCalculator = new DistanceProportionFareCalculator(LineFare.CHILD);

            int youthFare = originFare - 350;
            int expected = youthFare - (int)(youthFare * 0.5);
            assertThat(expected)
                .isEqualTo(childFareCalculator.calculateFare(distance));
        }

        Stream<Arguments> over10Fares() {
            return provideDistanceOver10AndFare();
        }

    }

    @DisplayName("노선별 추가 요금 테스트")
    @TestInstance(PER_CLASS)
    @Nested
    class 노선_추가_요금 {

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원) + 청소년 요금 할인 + 추가요금")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver10FareTest(int distance, int originFare, int additionalFare) {
            FareCalculator youthFareCalculator = new DistanceProportionFareCalculator(LineFare.YOUTH, additionalFare);

            int youthFare = originFare - 350 + additionalFare;
            int expected = youthFare - (int)(youthFare * 0.2);
            assertThat(expected)
                .isEqualTo(youthFareCalculator.calculateFare(distance));
        }

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원) + 어린이 요금 할인 + 추가요금")
        @MethodSource("over10Fares")
        @ParameterizedTest
        void calculateOver50FareTest(int distance, int originFare, int additionalFare) {
            FareCalculator childFareCalculator = new DistanceProportionFareCalculator(LineFare.CHILD, additionalFare);

            int youthFare = originFare - 350 + additionalFare;
            int expected = youthFare - (int)(youthFare * 0.5);
            assertThat(expected)
                .isEqualTo(childFareCalculator.calculateFare(distance));
        }

        Stream<Arguments> over10Fares() {
            return provideDistanceOver10AndFareAndAdditionalFare();
        }

    }

    public static Stream<Arguments> provideDistanceOver10AndFare() {
        return Stream.of(
            Arguments.of(15, 1350),
            Arguments.of(20, 1450),
            Arguments.of(25, 1550),
            Arguments.of(30, 1650),
            Arguments.of(35, 1750),
            Arguments.of(40, 1850),
            Arguments.of(45, 1950),
            Arguments.of(50, 2050)
        );
    }

    public static Stream<Arguments> provideDistanceOver50AndFare() {
        return Stream.of(
            Arguments.of(58, 2150),
            Arguments.of(66, 2250),
            Arguments.of(72, 2350),
            Arguments.of(80, 2450)
        );
    }

    public static Stream<Arguments> provideDistanceOver10AndFareAndAdditionalFare() {
        return Stream.of(
            Arguments.of(15, 1350, 100),
            Arguments.of(20, 1450, 200),
            Arguments.of(25, 1550, 300),
            Arguments.of(30, 1650, 400),
            Arguments.of(35, 1750, 500),
            Arguments.of(40, 1850, 600),
            Arguments.of(45, 1950, 700),
            Arguments.of(50, 2050, 800)
        );
    }
}
