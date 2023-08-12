package nextstep.line.domain.path;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public abstract class ShortPathFinder {

    protected List<Station> stations;
    protected List<Section> sections;

    public ShortPathFinder(List<Station> stations, List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public abstract boolean isSupport(ShortPathType type);

    abstract void setGraphEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph);

    public ShortPath getShortPath(Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(Section.class);
        setGraphVertex(graph);
        setGraphEdgeWeight(graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, Section> graphPath = dijkstraShortestPath.getPath(source, target);
        List<Section> sections = graphPath.getEdgeList();
        return new ShortPath(graphPath.getVertexList(), getDistance(sections), getDuration(sections));
    }

    private void setGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private Integer getDistance(List<Section> sections) {
        return sections.stream().mapToInt(Section::getDistance).sum();
    }

    private Integer getDuration(List<Section> sections) {
        return sections.stream().mapToInt(Section::getDuration).sum();
    }

}
