package nextstep.subway.unit;


import nextstep.subway.applicaion.FarePolicyHandler;
import nextstep.subway.applicaion.FarePolicyHandlerV1;
import nextstep.subway.domain.AgeFarePolicy;
import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineAddFarePolicy;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.PathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    Station 교대역;
    Station 강남역;
    Station 역삼역;
    Station 양재역;
    Station 매봉역;
    Station 남부터미널역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;

    Section 교대역_강남역_구간;
    Section 강남역_역삼역_구간;
    Section 교대역_남부터미널_구간;
    Section 남부터미널_양재역_구간;
    Section 강남역_양재역_구간;
    Section 양재역_매봉역_구간;

    FarePolicyHandler farePolicyHandler;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        매봉역 = new Station("매봉역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "orange", 1_000);
        신분당선 = new Line("신분당선", "red", 2_000);

        교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        강남역_역삼역_구간 = new Section(이호선, 강남역, 역삼역, 58, 10);
        교대역_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);
        남부터미널_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 11);
        강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 10, 2);
        양재역_매봉역_구간 = new Section(신분당선, 양재역, 매봉역, 59, 20);

        이호선.addSection(교대역_강남역_구간);
        이호선.addSection(강남역_역삼역_구간);
        삼호선.addSection(교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);
        신분당선.addSection(강남역_양재역_구간);
        신분당선.addSection(양재역_매봉역_구간);
        /**
         * 교대역    --- *2호선(10m, 3분)* ---     강남역 --- 2호선(58m, 10분)  ---  역삼역
         * |                                    |
         * 3호선(2m, 10분)                       신분당선(10m, 2분)
         * |                                    |
         * 남부터미널역  --- *3호선(3m, 11분)* ---   양재역 --- 신분당선(59m, 20분) --- 매봉역
         */

        farePolicyHandler = new FarePolicyHandlerV1();
        farePolicyHandler.chain(new DistanceFarePolicy())
                .chain(new LineAddFarePolicy())
                .chain(new AgeFarePolicy());

    }

    @DisplayName("최단 경로 조회 - 10km 이하 기본 요금")
    @Test
    void getShortsPathDistance() {
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DISTANCE);

        // when
        Path path = pathFinder.shortsPath(교대역, 강남역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(교대역, 강남역);
        assertThat(path.pathTotalDistance()).isEqualTo(10);
        assertThat(path.pathTotalDuration()).isEqualTo(3);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(1250);
    }

    @DisplayName("최소 시간 경로 조회 - 10km 이하 기본 요금")
    @Test
    void getShortsPathDuration() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);

        // when
        Path path = pathFinder.shortsPath(교대역, 양재역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(교대역, 강남역, 양재역);
        assertThat(path.pathTotalDistance()).isEqualTo(20);
        assertThat(path.pathTotalDuration()).isEqualTo(5);
    }

    @DisplayName("노선에 등록되지 않은 역")
    @Test
    void notExistsStationInLine() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);
        Station 노선에_없는_역 = new Station("가양역");

        // when
        assertThatThrownBy(() -> pathFinder.shortsPath(교대역, 노선에_없는_역))
                // then
                .isInstanceOf(PathException.class)
                .hasMessage("노선에 등록되지 않은 역입니다.");
    }

    @DisplayName("최단 경로 조회 시 요금 조회 -> 10km 초과, 5km마다 100원 추가")
    @Test
    void getShortsPathDurationDugi() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);

        // when
        Path path = pathFinder.shortsPath(교대역, 양재역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(교대역, 강남역, 양재역);
        assertThat(path.pathTotalDistance()).isEqualTo(20);
        assertThat(path.pathTotalDuration()).isEqualTo(5);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(3_450);
    }

    @DisplayName("최단 경로 조회 시 요금 조회 -> 50km 초과, 8km마다 100원 추가")
    @Test
    void getShortsPathDurationDugi22() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);

        // when
        Path path = pathFinder.shortsPath(강남역, 역삼역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(강남역, 역삼역);
        assertThat(path.pathTotalDistance()).isEqualTo(58);
        assertThat(path.pathTotalDuration()).isEqualTo(10);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(2150);
    }

    @DisplayName("최단 경로 조회 시 요금 조회 -> 50km 초과, 8km마다 100원 추가, 9km 초과")
    @Test
    void getShortsPathDurationDugi222() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);

        // when
        Path path = pathFinder.shortsPath(양재역, 매봉역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(양재역, 매봉역);
        assertThat(path.pathTotalDistance()).isEqualTo(59);
        assertThat(path.pathTotalDuration()).isEqualTo(20);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(4_250);
    }




    @DisplayName("경로 조회 시 노선 추가 요금")
    @Test
    void getShortsPathAdditionFare() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DURATION);

        // when
        Path path = pathFinder.shortsPath(양재역, 매봉역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(양재역, 매봉역);
        assertThat(path.pathTotalDistance()).isEqualTo(59);
        assertThat(path.pathTotalDuration()).isEqualTo(20);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(4250);
    }

    @DisplayName("경로 조회 시 추가 요금 - 환승 시 가장 비싼 노선의 추가 요금 적용")
    @Test
    void getShortsPathAdditionFareTransfer() {
        // given
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        PathFinder pathFinder = new PathFinder(lines, PathType.DISTANCE);

        // when
        Path path = pathFinder.shortsPath(강남역, 남부터미널역);

        // then
        List<Station> stations = path.getStations();
        assertThat(stations).containsExactly(강남역, 교대역, 남부터미널역);
        assertThat(path.pathTotalDistance()).isEqualTo(12);
        assertThat(path.pathTotalDuration()).isEqualTo(13);
        assertThat(farePolicyHandler.execute(0, 0, path)).isEqualTo(2_350);
    }
}
