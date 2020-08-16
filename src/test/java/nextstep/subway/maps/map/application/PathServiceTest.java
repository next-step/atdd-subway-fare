package nextstep.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

public class PathServiceTest {
    private List<Line> lines;
    private PathService pathService;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0, 10);
        line1.addLineStation(new LineStation(1L, null, 0, 0));
        line1.addLineStation(new LineStation(2L, 1L, 2, 2));

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0, 10);
        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(3L, 2L, 2, 1));

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 10);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        line3.addLineStation(new LineStation(4L, 1L, 1, 2));
        line3.addLineStation(new LineStation(3L, 4L, 2, 2));

        lines = Lists.newArrayList(line1, line2, line3);

        pathService = new PathService();
    }

    @Test
    void findPathByDistance() {
        // when
        SubwayPath subwayPath = pathService.findPath(lines, 1L, 3L, PathType.DISTANCE);

        // then
        assertThat(subwayPath.extractStationId().size()).isEqualTo(3);
        assertThat(subwayPath.extractStationId().get(0)).isEqualTo(1L);
        assertThat(subwayPath.extractStationId().get(1)).isEqualTo(4L);
        assertThat(subwayPath.extractStationId().get(2)).isEqualTo(3L);

    }

    @Test
    void findPathByDuration() {
        // when
        SubwayPath subwayPath = pathService.findPath(lines, 1L, 3L, PathType.DURATION);

        // then
        assertThat(subwayPath.extractStationId().size()).isEqualTo(3);
        assertThat(subwayPath.extractStationId().get(0)).isEqualTo(1L);
        assertThat(subwayPath.extractStationId().get(1)).isEqualTo(2L);
        assertThat(subwayPath.extractStationId().get(2)).isEqualTo(3L);
    }

    @Test
    void findPathOfFastestArrivalTime() {
        // when
        SubwayPath subwayPath = pathService.findPath(lines, 1L, 3L, PathType.ARRIVAL);

        // then
        assertThat(subwayPath.extractStationId().size()).isEqualTo(3);
        assertThat(subwayPath.extractStationId()).containsExactly(1L, 4L, 3L);
    }
}
