package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.LineStation;

import java.util.List;

public enum PathDirection {
    FORWARD, REVERS,
    ;

    public static PathDirection getDirection(List<LineStationEdge> lineStationEdges) {
        if (lineStationEdges.isEmpty() || lineStationEdges.size() == 1) {
            throw new RuntimeException("invalid line stations");
        }

        LineStation prevStation = lineStationEdges.get(0).getLineStation();
        LineStation nextstation = lineStationEdges.get(1).getLineStation();

        if (isForwardDirection(prevStation, nextstation)) {
            return FORWARD;
        }

        if (isReverseDirection(prevStation, nextstation)) {
            return REVERS;
        }

        throw new RuntimeException("invalid line stations");
    }

    private static boolean isForwardDirection(LineStation prevStation, LineStation nextstation) {
        return nextstation.getPreStationId().equals(prevStation.getStationId());
    }

    private static boolean isReverseDirection(LineStation prevStation, LineStation nextstation) {
        return prevStation.getPreStationId().equals(nextstation.getStationId());
    }
}
