package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum PathDirection {
    FORWARD(ForwardSubwayPathSection::new),
    REVERS(ReverseSubwayPathSection::new),
    ;

    private final Function<Line, ? extends SubwayPathSection> supplier;

    PathDirection(Function<Line, ? extends SubwayPathSection> supplier) {
        this.supplier = supplier;
    }

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

    public static PathDirection getDirection(LineStationEdge lineStationEdge) {
        Long sourceStationId = (Long) lineStationEdge.getSource();
        if (Objects.equals(sourceStationId, lineStationEdge.getLineStation().getPreStationId())) {
            return PathDirection.FORWARD;
        }
        return PathDirection.REVERS;
    }

    private static boolean isForwardDirection(LineStation prevStation, LineStation nextStation) {
        return Objects.equals(prevStation.getStationId(), nextStation.getPreStationId());
    }

    private static boolean isReverseDirection(LineStation prevStation, LineStation nextStation) {
        return Objects.equals(nextStation.getStationId(), prevStation.getPreStationId());
    }

    public SubwayPathSection createSection(Line line, List<LineStationEdge> sectionLineStationEdges) {
        SubwayPathSection subwayPathSection = supplier.apply(line);
        for (LineStationEdge sectionLineStationEdge : sectionLineStationEdges) {
            subwayPathSection.addLineStationEdge(sectionLineStationEdge);
        }
        return subwayPathSection;
    }
}
