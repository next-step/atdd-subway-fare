package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {
    private LineStation lineStation6;
    private LineStation lineStation7;

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 1000, 10);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        lineStation6 = new LineStation(4L, 1L, 1, 2);
        lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        List<LineStationEdge> lineStations = Lists.newArrayList(
                new LineStationEdge(lineStation6, line3),
                new LineStationEdge(lineStation7, line3)
        );
        subwayPath = new SubwayPath(lineStations);
    }

    @Test
    @DisplayName("경로에서 지나가는 노선 중 최대 노선별 요금을 계산한다")
    void getLineExtraFare() {
        //when
        Money money = subwayPath.calculateMaxLineExtraFare();

        //then
        assertThat(money).isEqualTo(Money.wons(1000));
    }
}