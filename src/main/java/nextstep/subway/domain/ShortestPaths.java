package nextstep.subway.domain;

import java.util.List;

public class ShortestPaths {

    private Path shortestDistancePath;

    private Path shortestDurationPath;

    private int shortestDistance;

    public ShortestPaths(Path shortestDurationPath, Path shortestDistancePath, int shortestDistance) {
        this.shortestDurationPath = shortestDurationPath;
        this.shortestDistancePath = shortestDistancePath;
        this.shortestDistance = shortestDistance;
    }

    public List<Section> getShortestDistanceSections() {
        return shortestDistancePath.getSections().getSections();
    }

    public List<Section> getShortestDurationSections() {
        return shortestDurationPath.getSections().getSections();
    }

    public Path getShortestDistancePath() {
        return shortestDistancePath;
    }

    public Path getShortestDurationPath() {
        return shortestDurationPath;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public String getShortestDistanceArrivalTime() {
        return shortestDistancePath.extractArrivalTime();
    }

    public String getShortestDurationArrivalTime() {
        return shortestDurationPath.extractArrivalTime();
    }
}
