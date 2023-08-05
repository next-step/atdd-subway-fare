package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SubwayMapTest {

    private Line line1;
    private Line line2;
    private Line line3;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;
    private Long station1Id = 11L;
    private Long station2Id = 12L;
    private Long station3Id = 13L;
    private Long station4Id = 14L;
    private Long station5Id = 15L;
    private Long station6Id = 16L;
    private Long station7Id = 17L;
    
    @BeforeEach
    void init() {

        line1 = spy(Line.class);
        line2 = spy(Line.class);
        line3 = spy(Line.class);

        station1 = spy(Station.class);
        station2 = spy(Station.class);
        station3 = spy(Station.class);
        station4 = spy(Station.class);
        station5 = spy(Station.class);
        station6 = spy(Station.class);
        station7 = spy(Station.class);


        when(station1.getId()).thenReturn(station1Id);
        when(station2.getId()).thenReturn(station2Id);
        when(station3.getId()).thenReturn(station3Id);
        when(station4.getId()).thenReturn(station4Id);
        when(station5.getId()).thenReturn(station5Id);
        when(station6.getId()).thenReturn(station6Id);
        when(station7.getId()).thenReturn(station7Id);

    }

    @Nested
    @DisplayName("거리 기준 경로 조회 테스트")
    public class SearchPathByDistanceTest {

        private static final int FIXED_DURATION = 1;


        /**
         *
         * line1: station1 - (9) - station2
         * line2: station2 - (2) - station3
         * line3: station3 - (10) - station4 - (11) - station1
         *
         * GIVEN 정점 4개, 간선 4개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN station1-station2-station3으로 경로가 나온다. 거리는 11이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 1 (환승 있는 경우1) - 정점 4개, 간선 4개")
        void searchPath1() {

            // given
            addSection(line1, station1, station2, 9);
            addSection(line2, station2, station3, 2);
            addSection(line3, station3, station4, 10);
            addSection(line3, station4, station1, 11);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DISTANCE);
            Path path = subwayMap.findPath(station1, station3);


            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id);
            assertThat(path.getDistance()).isEqualTo(11);
            assertThat(path.getDuration()).isEqualTo(2);
        }

        /**
         *
         * line1: station1 - (22) - station2
         * line2: station2 - (15) - station3
         * line3: station3 - (10) - station4 - (11) - station1
         *
         * GIVEN 정점 4개, 간선 4개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN: station1-station2-station3으로 경로가 나온다. 거리는 21이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 2 (환승 있는 경우2) - 정점 4개, 간선 4개")
        void searchPath2() {

            // given
            addSection(line1, station1, station2, 22);
            addSection(line2, station2, station3, 15);
            addSection(line3, station3, station4, 10);
            addSection(line3, station4, station1, 11);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DISTANCE);
            Path path = subwayMap.findPath(station1, station3);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station4Id, station3Id);
            assertThat(path.getDistance()).isEqualTo(21);
        }

        /**
         *
         * line1: station1 - (10) - station2 - (9) - station3
         *
         * GIVEN 정점 3개, 간선 2개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN: station1-station2-station3으로 경로가 나온다. 거리는 19이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 3 (환승 없는 경우) - 정점 3개, 간선 2개")
        void searchPath3() {

            // given
            addSection(line1, station1, station2, 10);
            addSection(line1, station2, station3, 9);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DISTANCE);
            Path path = subwayMap.findPath(station1, station3);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id);
            assertThat(path.getDistance()).isEqualTo(19);
        }

        /**
         *
         * line1: station1 - (2) - station2 - (3) - station3 - (3) - station4
         * line2: station5 - (7) - station2 - (2)  - station6 - (2) - station7 - (4) - station4
         *
         * GIVEN 정점 7개, 간선 7개
         * WHEN station1에서 station4으로 가는 경로 조회하면
         * THEN: station1-station2-station3-station4으로 경로가 나온다. 거리는 8이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 4 (환승 없는게 빠른 경우) - 정점 7개, 간선 7개")
        void searchPath4() {

            // given
            addSection(line1, station1, station2, 2);
            addSection(line1, station2, station3, 3);
            addSection(line1, station3, station4, 3);

            addSection(line2, station5, station2, 7);
            addSection(line2, station2, station6, 2);
            addSection(line2, station6, station7, 2);
            addSection(line2, station7, station4, 4);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2), PathType.DISTANCE);
            Path path = subwayMap.findPath(station1, station4);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id, station4Id);
            assertThat(path.getDistance()).isEqualTo(8);
        }

        /**
         *
         * line1: station1 - (12) - station2 - (10) - station3 - (20) - station4
         * line2: station5 - (7) - station2 - (2)  - station6 - (2) - station7 - (4) - station4
         *
         * GIVEN 정점 7개, 간선 7개
         * WHEN station1에서 station4으로 가는 경로 조회하면
         * THEN: station1-station2-station6-station7-station4으로 경로가 나온다. 거리는 20이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 5 (환승 하는게 빠른 경우) - 정점 7개, 간선 7개")
        void searchPath5() {

            // given
            addSection(line1, station1, station2, 12);
            addSection(line1, station2, station3, 10);
            addSection(line1, station3, station4, 20);

            addSection(line2, station5, station2, 7);
            addSection(line2, station2, station6, 2);
            addSection(line2, station6, station7, 2);
            addSection(line2, station7, station4, 4);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2), PathType.DISTANCE);
            Path path = subwayMap.findPath(station1, station4);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station6Id, station7Id, station4Id);
            assertThat(path.getDistance()).isEqualTo(20);
        }
        
        private void addSection(Line line, Station upStation, Station downStation, int distance) {
            line.addSection(upStation, downStation, distance, FIXED_DURATION);
        }
    }

    @Nested
    @DisplayName("소요시간 기준 경로 조회 테스트")
    public class SearchPathByDurationTest {

        private static final int FIXED_DISTANCE = 1;


        /**
         *
         * line1: station1 - (9) - station2
         * line2: station2 - (2) - station3
         * line3: station3 - (10) - station4 - (11) - station1
         *
         * GIVEN 정점 4개, 간선 4개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN station1-station2-station3으로 경로가 나온다. 소요시간은 11이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 1 (환승 있는 경우1) - 정점 4개, 간선 4개")
        void searchPath1() {

            // given
            addSection(line1, station1, station2, 9);
            addSection(line2, station2, station3, 2);
            addSection(line3, station3, station4, 10);
            addSection(line3, station4, station1, 11);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DURATION);
            Path path = subwayMap.findPath(station1, station3);


            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id);
            assertThat(path.getDuration()).isEqualTo(11);
            assertThat(path.getDistance()).isEqualTo(2);
        }

        /**
         *
         * line1: station1 - (22) - station2
         * line2: station2 - (15) - station3
         * line3: station3 - (10) - station4 - (11) - station1
         *
         * GIVEN 정점 4개, 간선 4개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN: station1-station2-station3으로 경로가 나온다. 소요시간은 21이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 2 (환승 있는 경우2) - 정점 4개, 간선 4개")
        void searchPath2() {

            // given
            addSection(line1, station1, station2, 22);
            addSection(line2, station2, station3, 15);
            addSection(line3, station3, station4, 10);
            addSection(line3, station4, station1, 11);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DURATION);
            Path path = subwayMap.findPath(station1, station3);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station4Id, station3Id);
            assertThat(path.getDuration()).isEqualTo(21);
        }

        /**
         *
         * line1: station1 - (10) - station2 - (9) - station3
         *
         * GIVEN 정점 3개, 간선 2개
         * WHEN station1에서 station3으로 가는 경로 조회하면
         * THEN: station1-station2-station3으로 경로가 나온다. 소요시간은 19이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 3 (환승 없는 경우) - 정점 3개, 간선 2개")
        void searchPath3() {

            // given
            addSection(line1, station1, station2, 10);
            addSection(line1, station2, station3, 9);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2, line3), PathType.DURATION);
            Path path = subwayMap.findPath(station1, station3);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id);
            assertThat(path.getDuration()).isEqualTo(19);
        }

        /**
         *
         * line1: station1 - (2) - station2 - (3) - station3 - (3) - station4
         * line2: station5 - (7) - station2 - (2)  - station6 - (2) - station7 - (4) - station4
         *
         * GIVEN 정점 7개, 간선 7개
         * WHEN station1에서 station4으로 가는 경로 조회하면
         * THEN: station1-station2-station3-station4으로 경로가 나온다. 소요시간은 8이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 4 (환승 없는게 빠른 경우) - 정점 7개, 간선 7개")
        void searchPath4() {

            // given
            addSection(line1, station1, station2, 2);
            addSection(line1, station2, station3, 3);
            addSection(line1, station3, station4, 3);

            addSection(line2, station5, station2, 7);
            addSection(line2, station2, station6, 2);
            addSection(line2, station6, station7, 2);
            addSection(line2, station7, station4, 4);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2), PathType.DURATION);
            Path path = subwayMap.findPath(station1, station4);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station3Id, station4Id);
            assertThat(path.getDuration()).isEqualTo(8);
        }

        /**
         *
         * line1: station1 - (12) - station2 - (10) - station3 - (20) - station4
         * line2: station5 - (7) - station2 - (2)  - station6 - (2) - station7 - (4) - station4
         *
         * GIVEN 정점 7개, 간선 7개
         * WHEN station1에서 station4으로 가는 경로 조은회하면
         * THEN: station1-station2-station6-station7-station4으로 경로가 나온다. 소요시간은 20이다.
         */
        @Test
        @DisplayName("경로 조회 테스트 5 (환승 하는게 빠른 경우) - 정점 7개, 간선 7개")
        void searchPath5() {

            // given
            addSection(line1, station1, station2, 12);
            addSection(line1, station2, station3, 10);
            addSection(line1, station3, station4, 20);

            addSection(line2, station5, station2, 7);
            addSection(line2, station2, station6, 2);
            addSection(line2, station6, station7, 2);
            addSection(line2, station7, station4, 4);

            // when
            SubwayMap subwayMap = new SubwayMap(List.of(line1, line2), PathType.DURATION);
            Path path = subwayMap.findPath(station1, station4);

            // then
            assertThat(path.getStations().stream().map(Station::getId).collect(Collectors.toList())).containsExactly(station1Id, station2Id, station6Id, station7Id, station4Id);
            assertThat(path.getDuration()).isEqualTo(20);
        }

        private void addSection(Line line, Station upStation, Station downStation, int duration) {
            line.addSection(upStation, downStation, FIXED_DISTANCE, duration);
        }
    }

}