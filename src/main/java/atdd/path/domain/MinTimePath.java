package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

public class MinTimePath {
    private List<Line> lines;
    private GraphPath<Long, DefaultWeightedEdge> graph;
    private static MinTimePathResponseView responseView = new MinTimePathResponseView();

    public MinTimePath(List<Line> lines, GraphPath<Long, DefaultWeightedEdge> graph) {
        this.lines = lines;
        this.graph = graph;
    }

    public MinTimePathResponseView findPathStationsResponseView
            (GraphPath<Long, DefaultWeightedEdge> graphPath, Long startId, Long endId) {
        responseView.insertStartId(startId);
        responseView.insertEndId(endId);
        List<Station> stations = graphPath.getVertexList().stream()
                .map(it -> findStation(it))
                .collect(Collectors.toList());
        responseView.insertStations(stations);
        return findLinesForPath(lines, stations);
    }

    private MinTimePathResponseView findLinesForPath(List<Line> lines, List<Station> stations) {
        Set<Line> linesForPath = new HashSet<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            int finalI = i;
            lines.stream()
                    .filter(it -> it.getStations().contains(stations.get(finalI)))
                    .filter(it -> it.getStations().contains(stations.get(finalI + 1)))
                    .forEach(it -> linesForPath.add(it));
        }
        responseView.insertLinesForPath(linesForPath);
        return findLineToStart(linesForPath, stations);
    }

    private MinTimePathResponseView findLineToStart(Set<Line> lines, List<Station> stations) {
        List<Line> collect = lines.stream()
                .filter(it -> it.getStations().contains(stations.get(0)))
                .filter(it -> it.getStations().contains(stations.get(1)))
                .collect(Collectors.toList());
        Line lineToStart = collect.get(0);
        return findTimeTableOfTheDay(lineToStart, stations);
    }

    private MinTimePathResponseView findTimeTableOfTheDay(Line lineToStart, List<Station> stations) {
        List<LocalTime> timeTables;
        Station firstStation = stations.get(0);

        List<Edge> collect1 = lineToStart.getEdges().stream()
                .filter(it -> it.getSourceStation().equals(stations.get(0)))
                .filter(it -> it.getTargetStation().equals(stations.get(1)))
                .collect(Collectors.toList());

        if (collect1.size() != 0) {
            timeTables = firstStation.showTimeTablesForUpDown(lineToStart, stations).getUp();
        }
        timeTables = firstStation.showTimeTablesForUpDown(lineToStart, stations).getDown();
        return findNextTimeToGetOnTheSubway(timeTables);
    }

    private MinTimePathResponseView findNextTimeToGetOnTheSubway(List<LocalTime> timetables) {
        LocalTime departAt;
        if (now().isAfter(timetables.get(timetables.size() - 1))
                || now().isBefore(timetables.get(0))) {
            departAt = timetables.get(0);
            responseView.insertDepartAt(departAt);
            return calculateTimeToArrive(departAt);
        }
        departAt = timetables.stream()
                .filter(it -> now().isAfter(it))
                .collect(Collectors.toList())
                .get(0);
        responseView.insertDepartAt(departAt);
        return calculateTimeToArrive(departAt);
    }

    private MinTimePathResponseView calculateTimeToArrive(LocalTime departBy) {
        double timeToTake = findTimeToTake(graph);
        LocalTime arriveBy = departBy.plusMinutes(Math.round(timeToTake));
        responseView.insertArriveBy(arriveBy);
        return responseView;
    }

    private double findTimeToTake(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        double distance_km = findDistance(graphPath);
        int velocity_kmPerHour = 50;  //지하철 속력 50km/h 로 계산
        double time_hour = distance_km / velocity_kmPerHour;
        double time_min = 60 * time_hour;
        return time_min;
    }

    private double findDistance(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        double distance_km = Math.round(graphPath.getWeight());
        responseView.insertDistance(distance_km);
        return distance_km;
    }

    private Station findStation(Long stationId) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}