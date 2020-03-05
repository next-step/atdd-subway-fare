package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

public class Graph {
    private List<Line> lines;

    public Graph(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Station> getShortestDistancePath(Long startId, Long endId) {
        return getPathStations(makeGraph(lines), startId, endId);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .forEach(it -> graph.addVertex(it.getId()));

        lines.stream()
                .flatMap(it -> it.getEdges().stream())
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getSourceStation().getId(), it.getTargetStation().getId()), it.getDistance()));
        return graph;
    }

    private List<Station> getPathStations(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Long startId, Long endId) {
        GraphPath<Long, DefaultWeightedEdge> path = new DijkstraShortestPath(graph).getPath(startId, endId);
        return path.getVertexList().stream()
                .map(it -> findStation(it))
                .collect(Collectors.toList());
    }

    private Station findStation(Long stationId) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public MinTimePathResponseView getMinTimePath(Long stationId, Long stationId4) {
        return getMinTimePathStations(makeGraph(lines), stationId, stationId4);
    }

    private MinTimePathResponseView getMinTimePathStations(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Long startId, Long endId) {
        List<GraphPath<Long, DefaultWeightedEdge>> paths = new KShortestPaths(graph, 1000).getPaths(startId, endId);
        GraphPath<Long, DefaultWeightedEdge> graphPath = paths.get(0);

        //지하철역 목록 구하기
        List<Station> stations = findPathStationsForMinTime(graphPath);

        //거리 구하기
        double distance_km = Math.round(graphPath.getWeight());

        //지나가는 라인 구하기
        Set<Line> linesForPath = findLinesForPath(lines, stations);

        //출발 라인 구하기
        Line lineToStart = findLineToStart(lines, stations);

        //출발 열차의 하루 시간표 구하기
        List<LocalTime> timeTableOfTheDay = lineToStart.findTimeTableOfTheDay(stations);

        //다음 열차 시간 구하기
        LocalTime departBy = findNextTimeToGetOnTheSubway(timeTableOfTheDay);

        //소요시간 구하기
        double timeToTake = findTimeToTake(graphPath);

        //도착시간 구하기
        LocalTime arriveBy = calculateTimeToArrive(departBy, timeToTake);

        MinTimePathResponseView responseView
                = new MinTimePathResponseView(startId, endId, stations, linesForPath,
                distance_km, departBy, arriveBy);

        return responseView;
    }

    public List<Station> findPathStationsForMinTime(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        //지하철역 목록 구하기
        List<Station> stations = graphPath.getVertexList().stream()
                .map(it -> findStation(it))
                .collect(Collectors.toList());
        return stations;
    }

    public Set<Line> findLinesForPath(List<Line> lines, List<Station> stations) {
        //전체 노선 목록과 경로의 지하철역 목록 주어졌을 때, 지나가는 라인 구하기
        Set<Line> linesForPath = new HashSet<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            int finalI = i;
            lines.stream()
                    .filter(it -> it.getStations().contains(stations.get(finalI)))
                    .filter(it -> it.getStations().contains(stations.get(finalI + 1)))
                    .forEach(it -> linesForPath.add(it));
        }
        return linesForPath;
    }

    public Line findLineToStart(List<Line> lines, List<Station> stations) {
        //전체 노선 목록과 경로의 지하철역 목록 주어졌을 때, 출발하는 역의 노선 구하기
        List<Line> collect = lines.stream()
                .filter(it -> it.getStations().contains(stations.get(0)))
                .filter(it -> it.getStations().contains(stations.get(1)))
                .collect(Collectors.toList());
        Line startLine = collect.get(0);
        return startLine;
    }

    public LocalTime findNextTimeToGetOnTheSubway(List<LocalTime> timetable) {
        //시간표와 현재시각 주어졌을 때, 다음 열차 시간 구하기
        if (now().isAfter(timetable.get(timetable.size() - 1)) || now().isBefore(timetable.get(0))) {
            return timetable.get(0);
        }
        return timetable.stream()
                .filter(it -> it.isAfter(now()))
                .filter(it -> it.isBefore(now()))
                .collect(Collectors.toList())
                .get(0);
    }

    private double findTimeToTake(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        //소요시간 구하기(min)
        double distance_km = graphPath.getWeight();
        int velocity_kmPerHour = 50;
        double time_hour = distance_km / velocity_kmPerHour;
        double time_min = 60 * time_hour;
        return time_min;
    }

    private LocalTime calculateTimeToArrive(LocalTime departBy, double timeToTake) {
        //도착시간 구하기
        return departBy.plusMinutes(Math.round(timeToTake));
    }
}
