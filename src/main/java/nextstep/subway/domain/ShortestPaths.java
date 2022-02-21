package nextstep.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;

public class ShortestPaths {

    Path shortestDistancePath;

    Path shortestTimePath;

    public Path getShortestDistancePath() {
        return null;
    }

    public Path getShortestDurationPath() {
        return null;
    }

    public int getShortestDistance() {
        GraphPath<Station, SectionEdge> result = null;

        return result.getEdgeList().stream()
            .mapToInt(value -> value.getSection().getDistance())
            .sum();
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
