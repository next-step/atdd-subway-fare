package atdd.path.domain;

import lombok.Getter;

@Getter
public class Edge {
    private Long id;
    private Station sourceStation;
    private Station targetStation;
    private int elapsedTime;
    private int distance;

    public Edge() {
    }

    public Edge(Long id, Station sourceStation, Station targetStation, int elapsedTime, int distance) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.elapsedTime = elapsedTime;
        this.distance = distance;
    }

    public boolean isTargetStation(long stationId) {
        if(targetStation.getId() == stationId) {
            return true;
        }

        return false;
    }

    public boolean isSourceStation(long stationId) {
        if(sourceStation.getId() == stationId) {
            return true;
        }

        return false;
    }

    public static Edge of(Station sourceStation, Station targetStation, int elapsedTime, int distance) {
        return new Edge(null, sourceStation, targetStation, elapsedTime, distance);
    }

    public boolean hasStation(Station station) {
        return sourceStation.equals(station) || targetStation.equals(station);
    }
}
