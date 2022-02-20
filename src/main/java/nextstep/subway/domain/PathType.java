package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.PathResponse;

import java.util.List;
import java.util.function.Function;

public enum PathType {
    DISTANCE("최단 거리", Section::getDistance),
    DURATION("최단 시간", Section::getDuration);

    private String description;
    private Function<Section, Integer> expression;

    PathType(String description, Function<Section, Integer> expression) {
        this.description = description;
        this.expression = expression;
    }

    public int weight(Section section) {
        return expression.apply(section);
    }

    public PathResponse createPathResponse(List<Station> pathStations, int weight, Lines lines) {
        PathType pathType = valueOf(name());
        if (pathType == DISTANCE) {
            Fare fare = new Fare(weight);
            return new PathResponse(pathStations, weight, lines.pathTotalDuration(pathStations), fare.calculateOverFare());
        }
        int distance = lines.pathTotalDistance(pathStations);
        Fare fare = new Fare(distance);
        return new PathResponse(pathStations,lines.pathTotalDistance(pathStations), weight, fare.calculateOverFare());
    }
}
