package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    private final SubwayPathSections subwayPathSections;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        subwayPathSections = SubwayPathSections.from(lineStationEdges);
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(subwayPathSections.getSourceStationId());
        stationIds.addAll(subwayPathSections.getSubwayPathSections().stream()
                .flatMap(section -> section.getLineStationEdges().stream())
                .map(it -> it.getLineStation().getStationId())
                .collect(Collectors.toList()));

        return stationIds;
    }

    public Long getSourceStationId() {
        return subwayPathSections.getSourceStationId();
    }

    public Long getTargetStationId() {
        return subwayPathSections.getTargetStationId();
    }

    public int calculateDuration() {
        return subwayPathSections.getSubwayPathSections().stream()
                .flatMap(section -> section.getLineStationEdges().stream())
                .mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return subwayPathSections.getSubwayPathSections().stream()
                .flatMap(section -> section.getLineStationEdges().stream())
                .mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public Line getExpensiveLine() {
        return subwayPathSections.getSubwayPathSections().stream()
                .flatMap(section -> section.getLineStationEdges().stream())
                .map(LineStationEdge::getLine)
                .max((line, line2) -> {
                    int extraFare = line.getExtraFare();
                    int extraFare2 = line2.getExtraFare();

                    return extraFare - extraFare2;
                }).orElseThrow(RuntimeException::new);
    }

    public LocalTime getAlightTime(LocalTime time) {
        return this.subwayPathSections.getAlightTime(time);
    }
}
