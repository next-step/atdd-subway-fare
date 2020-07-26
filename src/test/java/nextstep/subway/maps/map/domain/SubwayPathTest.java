package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {
    private Map<Long, Station> stations;
    private List<Line> lines;
    private LineStation lineStation2;
    private LineStation lineStation6;
    private LineStation lineStation7;

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN");
        line1.addLineStation(new LineStation(1L, null, 0, 0));
        lineStation2 = new LineStation(2L, 1L, 2, 2);
        line1.addLineStation(new LineStation(2L, 1L, 2, 2));

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED");
        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(3L, 2L, 2, 1));

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE");
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        lineStation6 = new LineStation(4L, 1L, 1, 2);
        lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        lines = Lists.newArrayList(line1, line2, line3);

        List<LineStationEdge> lineStations = Lists.newArrayList(
                new LineStationEdge(lineStation6, line3.getId()),
                new LineStationEdge(lineStation7, line3.getId())
        );
        subwayPath = new SubwayPath(lineStations);
    }

    @DisplayName("거리 별 요금 계산")
    @ParameterizedTest
    @CsvSource({"7, 1250", "12, 1350"})
    void calculateFareTest(int distance, int expected) {
        int fare = subwayPath.calculateFare(distance);

        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("이용거리 초과 요금 계산")
    @ParameterizedTest
    @CsvSource({"3, 100", "6, 200", "11, 300"})
    void calculateOverFare(int distance, int expected) {
        int fare = subwayPath.calculateOverFare(distance);

        assertThat(fare).isEqualTo(expected);
    }
}