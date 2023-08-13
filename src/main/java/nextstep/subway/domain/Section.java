package nextstep.subway.domain;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;
import java.util.Objects;

@Getter
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
        validate(distance, duration);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    private void validate(int distance, int duration) {
        if (distance == 0 || duration == 0) {
            throw new IllegalArgumentException();
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && duration == section.duration && Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upStation, downStation, distance, duration);
    }
}