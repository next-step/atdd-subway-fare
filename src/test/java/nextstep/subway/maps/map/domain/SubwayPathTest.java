package nextstep.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

class SubwayPathTest {
    private Map<Long, Station> stations;
    private List<Line> lines;
    private LineStation lineStation2;
    private LineStation lineStation6;
    private LineStation lineStation7;

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 1000, 10);
        서울_지하철_3호선.addLineStation(new LineStation(1L, null, 0, 0));
        lineStation6 = new LineStation(4L, 1L, 1, 2);
        lineStation7 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(lineStation6);
        서울_지하철_3호선.addLineStation(lineStation7);

        List<LineStationEdge> lineStations = Lists.newArrayList(
            new LineStationEdge(lineStation6, 서울_지하철_3호선),
            new LineStationEdge(lineStation7, 서울_지하철_3호선)
        );
        subwayPath = new SubwayPath(lineStations);
    }

    @DisplayName("경로를 지나가는 노선 중에서 최대 노선별 요금을 반환한다.")
    @Test
    void 최대_노선별_요금을_반환한다() {
        // when
        Money money = subwayPath.calculateMaxLineExtraFare();

        // then
        assertThat(money).isEqualTo(Money.drawNewMoney(1000));
    }

    @DisplayName("목적지의 예상 도착시간을 계산한다.")
    @Test
    void 예상_도착시간을_계산한다() {
        // when
        LocalDateTime arrivalTime = subwayPath.getArrivalTime(LocalDateTime.of(2020, 8, 24, 6, 15));

        // then
        assertThat(arrivalTime).isEqualTo(LocalDateTime.of(2020, 8, 24, 6, 19));
    }
}
