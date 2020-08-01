package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.utils.TestObjectUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class FareCalculatorTest {

    @DisplayName("기본 요금을 계산한다.")
    @Test
    void calculate() {
        // given
        FareCalculator fareCalculator = new FareCalculator();
        Line line = TestObjectUtils.createLine(1L, "test", "test");
        LineStation lineStation = new LineStation(1L, null, 5, 0);
        LineStation lineStation2 = new LineStation(2L, 1L, 5, 0);
        LineStationEdge lineStationEdge = new LineStationEdge(lineStation, line);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation, line);
        SubwayPath subwayPath = new SubwayPath(Arrays.asList(lineStationEdge, lineStationEdge2));

        // when
        FareContext fareContext = new FareContext(subwayPath);
        Fare fare = fareCalculator.calculate(fareContext);

        // then
        Assertions.assertThat(fare.getValue()).isEqualTo(1250);
    }
}