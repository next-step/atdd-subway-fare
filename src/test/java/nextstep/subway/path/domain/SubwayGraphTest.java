package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.DoesNotConnectedPathException;
import nextstep.subway.path.exception.SameStationPathSearchException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("경로 Domain 단위 테스트")
public class SubwayGraphTest {

    private Station savedStationGangNam;
    private Station savedStationYangJae;
    private Station savedStationGyoDae;
    private Station savedStationNambuTerminal;

    private Line line2;
    private Line line3;
    private Line lineNewBunDang;

    private List<Line> lines;
    private List<Section> sections;

    @BeforeEach
    void setUp() {
        savedStationGangNam = new Station(1L, "강남역");
        savedStationYangJae = new Station(2L, "양재역");
        savedStationGyoDae = new Station(3L, "교대역");
        savedStationNambuTerminal = new Station(4L, "남부터미널역");

        line2 = new Line(1L, "2호선", "bg-green-600");
        line2.addSection(savedStationGyoDae, savedStationGangNam, 7, 7);

        line3 = new Line(2L, "3호선", "bg-orange-600");
        line3.addSection(savedStationGyoDae, savedStationNambuTerminal, 3, 3);
        line3.addSection(savedStationNambuTerminal, savedStationYangJae, 3, 3);

        lineNewBunDang = new Line(3L, "신분당선", "bg-red-600");
        lineNewBunDang.addSection(savedStationGangNam, savedStationYangJae, 5, 5);

        lines = Lists.newArrayList(line2, line3, lineNewBunDang);

        sections = lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("지하철 최단 경로 조회")
    void findPath() {
        // given
        SubwayGraph subwayGraph = new SubwayGraph(sections, PathType.DISTANCE, savedStationGangNam, savedStationNambuTerminal);

        // when
        PathResult pathResult = subwayGraph.findPath();

        // then
        assertThat(pathResult.getStations()).hasSize(3);
        assertThat(pathResult.getStations()).containsExactly(savedStationGangNam, savedStationGyoDae, savedStationNambuTerminal);
        assertThat(pathResult.getTotalDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    void notEqualsSourceAndTarget() {
        // when & then
        assertThatExceptionOfType(SameStationPathSearchException.class)
                .isThrownBy(() -> {
                    SubwayGraph subwayGraph = new SubwayGraph(sections, PathType.DISTANCE, savedStationGangNam, savedStationGangNam);
                    subwayGraph.findPath();
                });
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    void notConnectedSourceAndTarget() {
        // given
        Station savedStationMyeongDong = new Station(10L, "명동역");

        // when & then
        assertThatExceptionOfType(DoesNotConnectedPathException.class)
                .isThrownBy(() -> {
                    SubwayGraph subwayGraph = new SubwayGraph(sections, PathType.DISTANCE, savedStationGangNam, savedStationMyeongDong);
                    subwayGraph.findPath();
                });
    }
}
