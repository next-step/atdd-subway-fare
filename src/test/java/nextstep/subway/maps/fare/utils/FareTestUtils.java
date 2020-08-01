package nextstep.subway.maps.fare.utils;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.utils.TestObjectUtils;

import java.util.Arrays;

public class FareTestUtils {

    public static SubwayPath sampleSubwayPath() {
        Line line = TestObjectUtils.createLine(1L, "test", "test");
        LineStation lineStation = new LineStation(1L, null, 5, 0);
        LineStation lineStation2 = new LineStation(2L, 1L, 5, 0);
        LineStationEdge lineStationEdge = new LineStationEdge(lineStation, line);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation, line);

        return new SubwayPath(Arrays.asList(lineStationEdge, lineStationEdge2));
    }
}
