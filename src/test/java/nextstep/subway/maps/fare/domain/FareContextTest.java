package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

class FareContextTest {

    @Test
    void getExtraFare() {
        // given
        Line line = new Line("test", "test", LocalTime.now(), LocalTime.now(), 10, 500);
        Line line2 = new Line("test", "test", LocalTime.now(), LocalTime.now(), 10, 100);
        Line line3 = new Line("test", "test", LocalTime.now(), LocalTime.now(), 10, 10000);
        LineStation lineStation = new LineStation(1L, null, 5, 0);
        LineStationEdge lineStationEdge = new LineStationEdge(lineStation, line);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation, line2);
        LineStationEdge lineStationEdge3 = new LineStationEdge(lineStation, line3);

        SubwayPath subwayPath = new SubwayPath(Arrays.asList(lineStationEdge, lineStationEdge2, lineStationEdge3));
        FareContext fareContext = new FareContext(subwayPath);

        // when
        int extraFare = fareContext.getExtraFare();

        // then
        Assertions.assertThat(extraFare).isEqualTo(10000);
    }
}