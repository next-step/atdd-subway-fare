package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.CascadeType;
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

    private int distance;

    private int duration;

    protected Section() {}

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public Section(Station upStation, Station downStation, int distance, int duration) {
        this(null, upStation, downStation, distance, duration);
    }

    public Section(Line line, Section section) {
        this(line, section.upStation, section.downStation, section.distance, section.duration);
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

    public int minusDistance(Section section) {
        return Math.abs(this.distance - section.distance);
    }

    public int plusDistance(Section section) {
        return Math.abs(this.distance + section.distance);
    }

    public int minusDuration(Section section) {
        return Math.abs(this.duration - section.duration);
    }

    public int plusDuration(Section section) {
        return Math.abs(this.duration + section.duration);
    }
}