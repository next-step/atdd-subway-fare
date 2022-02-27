package nextstep.subway.domain;

import java.util.List;
import java.util.Objects;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

@Entity
public class Section extends DefaultWeightedEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;
    private int duration;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation == station;
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation == station;
    }

    public boolean hasDuplicateSection(Station upStation, Station downStation) {
        return (this.upStation == upStation && this.downStation == downStation)
                || (this.upStation == downStation && this.downStation == upStation);
    }

    public PathDirection findPathDirection() {
        List<Station> stations = line.getStations();

        if (stations.get(0).equals(upStation)) {
            return PathDirection.DOWN;
        }

        boolean isEqualsCurrentStation;
        boolean isEqualsNextStation;
        for (int i = 1; i < stations.size(); i++) {
            isEqualsCurrentStation = upStation.equals(stations.get(i));
            isEqualsNextStation = downStation.equals(stations.get(i-1));

            PathDirection pathDirection = decidePathDirection(isEqualsCurrentStation, isEqualsNextStation);

            if (Objects.nonNull(pathDirection)) {
                return pathDirection;
            }
        }

        return PathDirection.DOWN;
    }

    private PathDirection decidePathDirection(boolean isEqualsCurrentStation, boolean isEqualsNextStation) {
        if (isEqualsCurrentStation && !isEqualsNextStation) {
            return PathDirection.DOWN;
        }

        if (isEqualsCurrentStation) {
            return PathDirection.UP;
        }

        return null;
    }
}