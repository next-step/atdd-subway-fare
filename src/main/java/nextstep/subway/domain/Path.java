package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class Path {
    private final List<Long> stations;
    private List<Section> sections;

    public Path(DijkstraShortestPath dijkstraShortestPath, Long source, Long target) {
        GraphPath graphPath;
        try {
            graphPath = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("경로에 존재하지 않는 역입니다.");
        }

        if(Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        this.stations = graphPath.getVertexList();
    }

    public Path(List<Long> vertexs, List<Section> sections) {
        this.stations = vertexs;
        this.sections = sections;
    }

    public List<Long> getStations() {
        return stations;
    }

    public int getDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public int getDuration() {
        return sections.stream()
                .mapToInt(Section::getDuration)
                .sum();
    }
}
