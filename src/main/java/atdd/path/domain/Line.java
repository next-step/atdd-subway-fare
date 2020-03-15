package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @JsonIgnore
    @Embedded
    private Edges edges;

    public Line(Long id, String name) {
        this(id, name, Collections.EMPTY_LIST, null, null, 0);
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(id, name, Collections.EMPTY_LIST, startTime, endTime, interval);
    }

    public Line(Long id, String name, List<Edge> edges, LocalTime startTime, LocalTime endTime, int interval) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = interval;
        this.edges = new Edges(edges);
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(null, name, Collections.EMPTY_LIST, startTime, endTime, interval);
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
        return intervalTime;
    }

    public List<Station> getStations() {
        return edges.getStations();
    }

    public void addEdge(Edge edge) {
        this.edges = this.edges.add(edge);
    }

    public Edges removeStation(Station station) {
        this.edges.removeStation(station);
        return this.edges;
    }

    public List<LocalTime> getTimetable(Long stationId, boolean isUp) {
        Long elapsedTime = this.getElapsedTimeBy(stationId, isUp);

        if (elapsedTime == 0) {
            return Collections.emptyList();
        }

        return IntStream.iterate(0, i -> i + this.intervalTime)
                .limit(Duration.between(this.startTime, this.endTime).dividedBy(this.intervalTime).toMinutes())
                .mapToObj(it -> this.startTime.plusMinutes(it).plusMinutes(elapsedTime))
                .collect(Collectors.toList());
    }


    private Long getElapsedTimeBy(Long stationId, boolean isUp) {
        Long elapsedTime = 0L;

        if (CollectionUtils.isEmpty(this.getEdges())) {
            return elapsedTime;
        }

        List<Edge> edges = this.edges.getEdges();

        if (!isUp) {
            Collections.reverse(edges);
        }

        for (Edge edge : edges) {
            if (isUp && edge.getSourceStation().getId().equals(stationId)) {
                break;
            }

            if (!isUp && edge.getTargetStation().getId().equals(stationId)) {
                break;
            }

            elapsedTime += edge.getElapsedTime();
        }

        return elapsedTime;
    }
}
