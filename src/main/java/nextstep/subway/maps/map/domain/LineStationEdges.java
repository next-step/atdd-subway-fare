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
        return lineStationEdges.stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("lineStationEdges is empty"))
                .getLineStation().getStationId();
    }

    public Long getTargetStationId() {
        int size = lineStationEdges.size();
        return lineStationEdges.stream().skip(size - 1).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("lineStationEdges is empty"))
                .getLineStation().getStationId();
    }
}
