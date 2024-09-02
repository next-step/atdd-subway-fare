package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.line.domain.entity.LineSections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
    }

    @ParameterizedTest
    @CsvSource({
        "5, 1250",
        "10, 1250",
        "11, 1350",
        "15, 1350",
        "50, 2050",
        "51, 2150",
        "60, 2250"
    })
    @DisplayName("거리에 따른 요금 계산")
    void calculateFareForVariousDistances(long distance, long expectedFare) {
        assertThat(fareCalculator.calculateFare(distance)).isEqualTo(expectedFare);
    }


    @ParameterizedTest
    @MethodSource("provideDistancesAndAdditionalFares")
    @DisplayName("거리와 추가 요금에 따른 총 요금 계산")
    void calculateTotalFareWithAdditionalFares(long distance, List<Long> additionalFares,
        long expectedTotalFare) {
        assertThat(FareCalculator.calculateFare(distance, additionalFares)).isEqualTo(
            expectedTotalFare);
    }

    private static Stream<Arguments> provideDistancesAndAdditionalFares() {
        return Stream.of(
            Arguments.of(10, Arrays.asList(0L, 500L, 900L), 2150L),
            Arguments.of(15, Collections.singletonList(700L), 2050L),
            Arguments.of(55, Arrays.asList(300L, 500L), 2650L),
            Arguments.of(5, Collections.emptyList(), 1250L),
            Arguments.of(60, Arrays.asList(1000L, 1500L), 3750L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDistancesSectionsAndAges")
    @DisplayName("거리, 구간, 나이에 따른 요금 계산")
    void calculateFareWithDistanceSectionsAndAge(long distance, List<LineSection> sections,
        Optional<Integer> age, long expectedFare) {
        assertThat(FareCalculator.calculateFare(distance, sections, age)).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideDistancesSectionsAndAges() {
        return Stream.of(
            Arguments.of(10, Collections.emptyList(), Optional.of(20), 1250L),
            Arguments.of(15, createSections(500L), Optional.of(15), 1200L),
            Arguments.of(51, createSections(1000L), Optional.of(8), 1400L),
            Arguments.of(60, createSections(1500L), Optional.empty(), 3750L)
        );
    }

    private static List<LineSection> createSections(Long additionalFare) {
        Station station1 = new Station("Station1");
        Station station2 = new Station("Station2");
        Line line = new Line("Line1", "Red", new LineSections(), additionalFare);
        return Collections.singletonList(new LineSection(line, station1, station2, 1L, 2L));
    }
}