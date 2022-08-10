package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @Embedded
    private Distance distance = new Distance();
    @Embedded
    private Duration duration = new Duration();

    public Section() {

    }

    private Section(Line line, Station upStation, Station downStation, Distance distance, Duration duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance, Duration duration) {
        return new Section(line, upStation, downStation, distance, duration);
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

    public Distance decreasedDistance(Section section) {
        return distance.decrease(section.distance);
    }

    public Duration decreasedDuration(Section section) {
        return this.duration.decrease(section.duration);
    }

    public Distance increasedDistance(Section section) {
        return this.distance.increase(section.distance);
    }

    public Duration increasedDuration(Section section) {
        return this.duration.increase(section.duration);
    }

    public int getDistance() {
        return this.distance.getDistance();
    }

    public int getDuration() {
        return this.duration.getDuration();
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
}