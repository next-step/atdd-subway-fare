package nextstep.subway.maps.map.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.LineStation;

public enum PathDirection {
    UPBOUND,
    DOWNBOUND;

    public static PathDirection findBySourceId(LineStation lineStation, Long sourceId) {
        if (Objects.equals(lineStation.getPreStationId(), sourceId)) {
            return PathDirection.UPBOUND;
        }
        if (Objects.equals(lineStation.getStationId(), sourceId)) {
            return PathDirection.DOWNBOUND;
        }
        throw new IllegalStateException("please check again about your input.");
    }

    public List<Long> extractStationIds(List<LineStationEdge> lineStationEdges) {
        List<Long> stationIds = Lists.newArrayList(this.getSourceId(lineStationEdges));
        stationIds.addAll(lineStationEdges.stream()
            .mapToLong(this::getNextStationId)
            .distinct()
            .boxed()
            .collect(Collectors.toList())
        );
        return stationIds;
    }

    private Long getNextStationId(LineStationEdge lineStationEdge) {
        return UPBOUND == this
            ? lineStationEdge.getLineStation().getStationId()
            : lineStationEdge.getLineStation().getPreStationId();
    }

    private Long getSourceId(List<LineStationEdge> lineStationEdges) {
        LineStation lineStation = lineStationEdges.get(0).getLineStation();
        return UPBOUND == this
            ? lineStation.getPreStationId()
            : lineStation.getStationId();
    }
}
