package nextstep.subway.domain;

import java.util.List;

public class ShortestPaths {

    private Path shortestDistancePath;

    private Path shortestDurationPath;

    public ShortestPaths(Path shortestDurationPath, Path shortestDistancePath) {
        this.shortestDurationPath = shortestDurationPath;
        this.shortestDistancePath = shortestDistancePath;
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
        return shortestDistancePath.extractDistance();
    }

    public String getShortestDistanceArrivalTime() {
        return shortestDistancePath.extractArrivalTime();
    }

    public String getShortestDurationArrivalTime() {
        return shortestDurationPath.extractArrivalTime();
    }
}
