package nextstep.subway.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayMap;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.path.exception.StationNotInGivenLinesException;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("최단경로 단위 테스트")
class SubwayMapTest {
    public static final String DISTANCE = "DISTANCE";
    public static final String DURATION = "DURATION";
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        이호선 = new Line("2호선", "green", 0, new Section(교대역, 강남역, 10, 1));
        신분당선 = new Line("신분당선", "red", 0, new Section(강남역, 양재역, 10, 1));
        삼호선 = new Line("3호선", "orange", 0, new Section(교대역, 남부터미널역, 2, 10));

        삼호선.registerSection(new Section(남부터미널역, 양재역, 3, 12));
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void findPathByDistance() {
        // when
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선), DISTANCE);
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getTotalDistance()).isEqualTo(5);
        assertThat(path.getTotalDuration()).isEqualTo(22);
    }

    @DisplayName("최소 시간 경로 조회")
    @Test
    void findPathByDuration() {
        // when
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선), DURATION);
        Path path = subwayMap.findPath(교대역, 양재역);


        // then
        assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
        assertThat(path.getTotalDistance()).isEqualTo(20);
        assertThat(path.getTotalDuration()).isEqualTo(2);
    }

    @DisplayName("경로가 없을 경우 예외가 발생한다.")
    @Test
    void constructFailByPathNotFound() {
        // given: 역 정보와 노선 정보가 주어진다.
        Station 증미역 = new Station(5L, "증미역");
        Station 여의도역 = new Station(6L, "여의도역");

        Line 구호선 = new Line("9호선", "brown", 0, new Section(증미역, 여의도역, 2, 10));

        // when
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선, 구호선), DISTANCE);

        // then
        assertThatThrownBy(() -> subwayMap.findPath(교대역, 여의도역))
                .isInstanceOf(PathNotFoundException.class);
    }

    @DisplayName("노선에 없는 역 조회")
    @Test
    void constructFailByNotInLines() {
        // given
        Station 노선에_없는_역 = new Station(6L, "노선에 없는 역");

        // when
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선), DISTANCE);

        // then
        assertThatThrownBy(() -> subwayMap.findPath(교대역, 노선에_없는_역))
                .isInstanceOf(StationNotInGivenLinesException.class);
    }
}