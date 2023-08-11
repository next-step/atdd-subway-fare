package nextstep.line.domain;

import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DurationShortPathFinder extends ShortPathFinder {

    public DurationShortPathFinder(List<Station> stations, List<Section> sections) {
        super(stations, sections);
    }

    @Override
    public boolean isSupport(ShortPathType type) {
        return type.isDuration();
    }

    @Override
    public ShortPath getShortPath(Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setGraphVertex(graph);
        setGraphEdgeWeight(graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
        return new ShortPath(graphPath.getVertexList(), graphPath.getWeight());
    }

    private void setGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setGraphEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDuration());
        }
    }


}
