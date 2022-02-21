package nextstep.subway.domain;

import java.util.List;

public class ShortestPaths {

    Path shortestDistancePath;

    Path shortestDurationPath;

    int shortestDistance;

    public ShortestPaths(Path shortestDurationPath, Path shortestDistancePath, int shortestDistance) {
        this.shortestDurationPath = shortestDurationPath;
        this.shortestDistancePath = shortestDistancePath;
        this.shortestDistance = shortestDistance;
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

    public String getShortestArrivalTime() {
        return null;
    }

    public List<Section> getShortestDistanceSections() {
        return null;
    }

    public List<Section> getShortestDurationSections() {
        return null;
    }
}
