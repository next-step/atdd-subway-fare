package nextstep.subway.path.application.dto;

import nextstep.subway.path.domain.CustomWeightedEdge;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResponse {
    private final List<Station> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public PathResponse(GraphPath<Station, CustomWeightedEdge> path) {
        this.stations = path.getVertexList();
        this.distance = totalDistance(path);
        this.duration = totalDuration(path);
        this.fare = FareCalculator.calculateFare(distance);
    }

    private int totalDistance(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDistance).sum();
    }

    private int totalDuration(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDuration).sum();
    }

    public PathResponse(List<Station> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }
}
