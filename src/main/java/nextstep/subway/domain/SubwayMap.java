package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private SimpleDirectedWeightedGraph<Station, SectionEdge> graph  = new SimpleDirectedWeightedGraph<>(SectionEdge.class);;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType) {

        Sections sections = findPathByStationEdge(source, target, pathType);
        int shortDistance = findShortDistance(source,target, pathType, sections);

        return new Path(sections, shortDistance);
    }

    private void addStation(){
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

    }

    private void addStationEdge(PathType pathType){
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, pathType.getValue(it));
                });
    }

    private void addReverseStationEdge(PathType pathType){
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, pathType.getValue(it));
                });
    }

    private int findShortDistance(Station source, Station target, PathType pathType, Sections sections){
        if(pathType.name().equals("DURATION")){
            return findPathByStationEdge(source, target,  PathType.valueOf("DISTANCE")).totalDistance();
        }
        return sections.totalDistance();
    }

    private Sections findPathByStationEdge(Station source, Station target, PathType pathType){
        graph  = new SimpleDirectedWeightedGraph<>(SectionEdge.class);;
        addStation();
        addStationEdge(pathType);
        addReverseStationEdge(pathType);
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());
        return new Sections(sections);
    }

}
