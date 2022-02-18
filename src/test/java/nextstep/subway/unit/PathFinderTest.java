package nextstep.subway.unit;


import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.PathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;

    Section 교대역_강남역_구간;
    Section 교대역_남부터미널_구간;
    Section 남부터미널_양재역_구간;
    Section 강남역_양재역_구간;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "orange");
        신분당선 = new Line("신분당선", "red");

        교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        교대역_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);
        남부터미널_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 11);
        강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 10, 2);

        이호선.addSection(교대역_강남역_구간);
        삼호선.addSection(교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);
        신분당선.addSection(강남역_양재역_구간);
        /**
         * 교대역    --- *2호선(10m, 3분)* ---     강남역
         * |                                    |
         * 3호선(2m, 10분)                       신분당선(10m, 2분)
         * |                                    |
         * 남부터미널역  --- *3호선(3m, 11분)* ---   양재역
         */
    }

    @DisplayName("최단 경로 조회")
    @Test
    void getShortsPathDistance() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines);

        // when
        List<Station> stations = pathFinder.shortsPathStations(교대역, 양재역, PathType.DISTANCE);
        Path path = pathFinder.shortsPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(stations).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5);
        assertThat(path.getDuration()).isEqualTo(21);
    }

    @DisplayName("최소 시간 경로 조회")
    @Test
    void getShortsPathDuration() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines);

        // when
        List<Station> stations = pathFinder.shortsPathStations(교대역, 양재역, PathType.DURATION);
        Path path = pathFinder.shortsPath(교대역, 양재역, PathType.DURATION);

        // then
        assertThat(stations).containsExactly(교대역, 강남역, 양재역);
        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getDuration()).isEqualTo(5);
    }

    @DisplayName("노선에 등록되지 않은 역")
    @Test
    void notExistsStationInLine() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines);
        Station 노선에_없는_역 = new Station("역삼역");

        // when
        assertThatThrownBy(() -> pathFinder.shortsPathStations(교대역, 노선에_없는_역, PathType.DURATION))
                // then
                .isInstanceOf(PathException.class)
                .hasMessage("노선에 등록되지 않은 역입니다.");
    }
}
