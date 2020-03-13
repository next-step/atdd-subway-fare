package atdd.path.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Embeddable
public class Edges {
    @OneToMany(mappedBy = "line")
    private List<Edge> edges = new ArrayList<>();

    public Edges(List<Edge> edges) {
        checkValidEdges(edges);
        this.edges = edges;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    private void checkValidEdges(List<Edge> edges) {
        if (CollectionUtils.isEmpty(edges)) {
            return;
        }

        if (getStations(edges).size() != edges.size() + 1) {
            throw new RuntimeException();
        }
    }

    public List<Station> getStations() {
        return getStations(this.edges);
    }

    private List<Station> getStations(List<Edge> edges) {
        if (CollectionUtils.isEmpty(edges)) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList();
        Station lastStation = findFirstStation(edges);
        stations.add(lastStation);

        while (true) {
            Station nextStation = findNextStationOf(edges, lastStation);
            if (nextStation == null) {
                break;
            }
            stations.add(nextStation);
            lastStation = nextStation;
        }

        return stations;
    }

    private Station findFirstStation(List<Edge> edges) {
        List<Station> sourceStations = edges.stream()
                .map(it -> it.getTargetStation())
                .collect(Collectors.toList());

        return edges.stream()
                .map(it -> it.getSourceStation())
                .filter(it -> !sourceStations.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Station findNextStationOf(List<Edge> edges, Station firstStation) {
        return edges.stream()
                .filter(it -> firstStation.equals(it.getSourceStation()))
                .map(it -> it.getTargetStation())
                .findFirst()
                .orElse(null);
    }

    public void removeStation(Station station) {
        List<Edge> replaceEdge = this.edges.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (replaceEdge.size() == 0) {
            throw new RuntimeException();
        }

        List<Edge> newEdges = this.edges.stream()
                .filter(it -> !replaceEdge.contains(it))
                .collect(Collectors.toList());

        if (replaceEdge.size() == 1) {
            this.edges = newEdges;
        }

        Edge newEdge = Edge.of(getSourceStationOf(station), getTargetStationOf(station), sum(replaceEdge));
        newEdges.add(newEdge);

        this.edges = newEdges;
    }

    private Integer sum(List<Edge> replaceEdge) {
        return replaceEdge.stream().map(it -> it.getDistance()).reduce(0, Integer::sum);
    }

    private Station getSourceStationOf(Station station) {
        return this.edges.stream()
                .filter(it -> station.equals(it.getTargetStation()))
                .map(it -> it.getSourceStation())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Station getTargetStationOf(Station station) {
        return this.edges.stream()
                .filter(it -> station.equals(it.getSourceStation()))
                .map(it -> it.getTargetStation())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public Edges add(Edge edge) {
        List<Edge> newEdges = this.edges.stream().collect(Collectors.toList());
        newEdges.add(edge);
        return new Edges(newEdges);
    }

    public Edge findNewEdge(List<Edge> oldEdges) {
        return this.edges.stream()
                .filter(it -> !oldEdges.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
