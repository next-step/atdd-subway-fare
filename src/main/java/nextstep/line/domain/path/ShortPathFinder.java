package nextstep.line.domain.path;

import nextstep.auth.principal.UserPrincipal;
import nextstep.line.domain.Section;
import nextstep.line.domain.Sections;
import nextstep.line.domain.fare.DistanceFarePolicies;
import nextstep.line.domain.fare.FareCalculator;
import nextstep.member.domain.Member;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public abstract class ShortPathFinder {

    protected final List<Station> stations;
    protected final List<Section> sections;
    private final FareCalculator fareCalculator;

    public ShortPathFinder(List<Station> stations, List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
        this.fareCalculator = new FareCalculator();
    }

    public abstract boolean isSupport(ShortPathType type);

    abstract void setGraphEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph);

    public ShortPath getShortPath(Station source, Station target, Member member) {
        GraphPath<Station, Section> graphPath = getPath(source, target);
        Sections sections = new Sections(graphPath.getEdgeList());
        List<Station> stations = sections.getStations(graphPath.getStartVertex());
        Integer distance = sections.getDistance();
        Integer duration = sections.getDuration();
        Integer fare = fareCalculator.getFare(sections, member);
        return new ShortPath(stations, distance, duration, fare);
    }

    private GraphPath<Station, Section> getPath(Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(Section.class);
        setGraphVertex(graph);
        setGraphEdgeWeight(graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void setGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

}
