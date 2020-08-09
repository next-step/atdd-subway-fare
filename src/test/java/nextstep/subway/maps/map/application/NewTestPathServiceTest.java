package nextstep.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

public class NewTestPathServiceTest {
    private List<Line> lines;
    private PathService pathService;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line 서울_지하철_2호선 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        LineStation 서울_지하철_2호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 서울_지하철_2호선_양재역 = new LineStation(2L, 1L, 2, 2);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_교대역);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_양재역);

        Line 신분당선 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0);
        LineStation 신분당선_강남역 = new LineStation(2L, null, 0, 0);
        LineStation 신분당선_양재역 = new LineStation(3L, 2L, 2, 1);
        신분당선.addLineStation(신분당선_강남역);
        신분당선.addLineStation(신분당선_양재역);

        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        LineStation 서울_지하철_3호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 서울_지하철_3호선_남부터미널역 = new LineStation(4L, 1L, 1, 2);
        LineStation 서울_지하철_3호선_양재역 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_교대역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_남부터미널역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_양재역);

        lines = Lists.newArrayList(서울_지하철_2호선, 신분당선, 서울_지하철_3호선);
        pathService = new PathService();
    }

    @Test
    void findPathByArrivalTime() {
        //given
        LocalDateTime departTime = LocalDateTime.of(2020, 8, 24, 14, 0);
        // when
        SubwayPath subwayPath = pathService.findPathByArrivalTime(lines, 1L, 3L, departTime);

        // then
        assertThat(subwayPath.extractStationId().size()).isEqualTo(3);
        assertThat(subwayPath.extractStationId()).containsExactly(1L, 4L, 3L);
    }
}
