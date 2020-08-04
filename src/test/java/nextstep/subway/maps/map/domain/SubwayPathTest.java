package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {
    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 1000, 5);
        LineStation lineStation1 = new LineStation(1L, null, 0, 0);
        LineStation lineStation2 = new LineStation(4L, 1L, 1, 2);
        LineStation lineStation3 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation1);
        line3.addLineStation(lineStation2);
        line3.addLineStation(lineStation3);

        LineStationEdge lineStationEdge1 = new LineStationEdge(lineStation1, line3);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation2, line3);
        LineStationEdge lineStationEdge3 = new LineStationEdge(lineStation3, line3);
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(lineStationEdge1, lineStationEdge2, lineStationEdge3);
        subwayPath = new SubwayPath(lineStationEdges, 1L);
    }

    @Test
    @DisplayName("경로에서 지나가는 노선 중 최대 노선별 요금을 계산한다")
    void getLineExtraFare() {
        //when
        Money money = subwayPath.calculateMaxLineExtraFare();

        //then
        assertThat(money).isEqualTo(Money.wons(1000));
    }

    @DisplayName("경로의 목적지 도착시간을 계산한다.")
    @Test
    void getArrivalTime() {
        //when
        LocalDateTime arrivalTime = subwayPath.getArrivalTime(LocalDateTime.of(2020, 7, 22, 6, 15));

        //then
        assertThat(arrivalTime)
                .isEqualTo(LocalDateTime.of(2020, 7, 22, 6, 19));
    }
}