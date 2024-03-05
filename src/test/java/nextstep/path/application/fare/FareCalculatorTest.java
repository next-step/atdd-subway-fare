package nextstep.path.application.fare;

import nextstep.common.fixture.LineFactory;
import nextstep.common.fixture.SectionFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.path.domain.Path;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {
    static final Station 강남역 = StationFactory.createStation("강남역");
    static final Station 선릉역 = StationFactory.createStation("선릉역");
    static final Section 임시_구간 = SectionFactory.createSection(강남역, 선릉역, 10, 10);
    static final Line 무료_노선 = LineFactory.createLine("2호선", "green", 0L, 임시_구간);
    static final Line 오백원_노선 = LineFactory.createLine("3호선", "orange", 500L, 임시_구간);
    static final Line 천원_노선 = LineFactory.createLine("신분당선", "blue", 1000L, 임시_구간);

    @ParameterizedTest
    @MethodSource("providePath")
    @DisplayName("요금 정책들을 통해 최종 요금을 반환 받을 수 있다.")
    void fareCalculatorTest(final Path path, final Integer age, final long expected) {
        final FareCalculator fareCalculator = new FareCalculator();

        final long calculated = fareCalculator.calculate(path, age);

        assertThat(calculated).isEqualTo(expected);
    }

    private static Stream<Arguments> providePath() {
        return Stream.of(
                Arguments.of(new FakePath(List.of(무료_노선, 오백원_노선, 천원_노선), 12), 10, 1350L)
                , Arguments.of(new FakePath(List.of(무료_노선, 오백원_노선, 천원_노선), 12), null, 2350L)
                , Arguments.of(new FakePath(List.of(무료_노선, 오백원_노선), 67), 13, 2350L)
                , Arguments.of(new FakePath(List.of(오백원_노선, 천원_노선), 10), 19, 2250L)
        );
    }

    private static class FakePath extends Path {
        private final List<Line> usedLine;
        private final int distance;

        public FakePath(final List<Line> usedLine, final int distance) {
            super(List.of(), List.of());
            this.distance = distance;
            this.usedLine = usedLine;
        }

        @Override
        public int getDistance() {
            return distance;
        }

        @Override
        public List<Station> getStations() {
            return super.getStations();
        }

        @Override
        public int getDuration() {
            return super.getDuration();
        }

        @Override
        public List<Line> getUsedLine() {
            return usedLine;
        }
    }
}
