package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.LineStation;

import java.util.List;
import java.util.Objects;

public enum PathDirection {
    FORWARD, REVERS,
    ;

    public static PathDirection getDirection(List<LineStationEdge> lineStationEdges) {
        if (lineStationEdges.isEmpty() || lineStationEdges.size() == 1) {
            throw new RuntimeException("invalid line stations");
        }

        LineStation prevStation = lineStationEdges.get(0).getLineStation();
        LineStation nextStation = lineStationEdges.get(1).getLineStation();

        if (isForwardDirection(prevStation, nextStation)) {
            return FORWARD;
        }

        if (isReverseDirection(prevStation, nextStation)) {
            return REVERS;
        }

        throw new RuntimeException("invalid line stations");
    }

    private static boolean isForwardDirection(LineStation prevStation, LineStation nextStation) {
        return Objects.equals(prevStation.getStationId(), nextStation.getPreStationId());
    }

    private static boolean isReverseDirection(LineStation prevStation, LineStation nextStation) {
        return Objects.equals(nextStation.getStationId(), prevStation.getPreStationId());
    }
}
