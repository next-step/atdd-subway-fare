package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
                .map(it -> it.getLineStation().getStationId())
                .collect(Collectors.toList()));

        return stationIds;
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

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public Line getExpensiveLine() {
        return this.lineStationEdges.stream()
                .map(LineStationEdge::getLine)
                .max((line, line2) -> {
                    int extraFare = line.getExtraFare();
                    int extraFare2 = line2.getExtraFare();

                    return extraFare - extraFare2;
                }).orElseThrow(RuntimeException::new);
    }
}
