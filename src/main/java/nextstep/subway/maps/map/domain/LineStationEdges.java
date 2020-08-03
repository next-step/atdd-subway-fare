package nextstep.subway.maps.map.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineStationEdges {
    private final List<LineStationEdge> lineStationEdges = new ArrayList<>();

    public LineStationEdges(LineStationEdge... lineStationEdges) {
        this.lineStationEdges.addAll(Arrays.asList(lineStationEdges));
    }

    public Long getSourceStationId() {
        return null;
    }

    public Long getTargetStationId() {
        return null;
    }
}
