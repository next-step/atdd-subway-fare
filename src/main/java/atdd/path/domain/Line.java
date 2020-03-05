package atdd.path.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int interval;
    private Edges edges;

    public Line(Long id, String name) {
        this(id, name, Collections.EMPTY_LIST, null, null, 0);
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(id, name, Collections.EMPTY_LIST, startTime, endTime, interval);
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(null, name, Collections.EMPTY_LIST, startTime, endTime, interval);
    }

    public Line(Long id, String name, List<Edge> edges, LocalTime startTime, LocalTime endTime, int interval) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.edges = new Edges(edges);
    }

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int interval) {
        return new Line(name, startTime, endTime, interval);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Edge> getEdges() {
        return edges.getEdges();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getInterval() {
        return interval;
    }

    public List<Station> getStations() {
        return edges.getStations();
    }

    public void addEdge(Edge edge) {
        this.edges = this.edges.add(edge);
    }

    public Edges removeStation(Station station) {
        this.edges = this.edges.removeStation(station);
        return this.edges;
    }

    public List<LocalTime> makeTimeTable(LocalTime start, LocalTime end) {
        List<LocalTime> timeTable = new ArrayList<>();
        timeTable.add(start);
        LocalTime nextTime = start.plusMinutes(this.interval);
        while (nextTime.isBefore(end)) {
            timeTable.add(nextTime);
            nextTime = nextTime.plusMinutes(this.interval);
        }
        if (nextTime.equals(end)) {
            timeTable.add(nextTime);
        }
        return timeTable;
    }

    public List<LocalTime> findTimeTableOfTheDay(List<Station> stations) {
        //출발하는 노선이 지금 라인이고, 경로의 지하철역 목록 주어졌을 때 시간표 구하기
        List<LocalTime> timeTable;
        List<Edge> collect1 = this.getEdges().stream()
                .filter(it -> it.getSourceStation().equals(stations.get(0)))
                .filter(it -> it.getTargetStation().equals(stations.get(1)))
                .collect(Collectors.toList());
        if (collect1.size() != 0) {
            timeTable = stations.get(0).showTimeTablesForUpDown(this, stations).getDown();
        }
        timeTable = stations.get(0).showTimeTablesForUpDown(this, stations).getUp();
        return timeTable;
    }
}
