package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.LineStation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum PathDirection {
    FORWARD, REVERSE;

    public static PathDirection findBySourceId(LineStation lineStation, Long sourceId) {
        if (Objects.equals(lineStation.getPreStationId(), sourceId)) {
            return PathDirection.FORWARD;
        }
        if (Objects.equals(lineStation.getStationId(), sourceId)) {
            return PathDirection.REVERSE;
        }

        throw new RuntimeException("잘못된 경로가 설정되었습니다. ");
    }

    public List<Long> extractStationIds(List<LineStationEdge> lineStationEdges) {
        List<Long> stationIds = Lists.newArrayList(this.getSourceId(lineStationEdges));
        stationIds.addAll(lineStationEdges.stream()
                .mapToLong(this::getNextStationId)
                .distinct()
                .boxed()
                .collect(Collectors.toList()));
        return stationIds;
    }

    private Long getNextStationId(LineStationEdge lineStationEdge) {
        return FORWARD == this ? lineStationEdge.getLineStation().getStationId() : lineStationEdge.getLineStation().getPreStationId();
    }

    private Long getSourceId(List<LineStationEdge> lineStationEdges) {
        LineStation lineStation = lineStationEdges.get(0).getLineStation();
        return FORWARD == this ? lineStation.getPreStationId() : lineStation.getStationId();
    }
}
