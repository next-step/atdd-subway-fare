package nextstep.subway.domain;

import nextstep.exception.line.MinimumDistanceException;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

@Entity
public class Section extends DefaultWeightedEdge {

    private static final int MIN_DISTANCE = 1;
    private static final int MIN_DURATION = 1;

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

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        validate(distance, duration);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance, int duration) {
        return new Section(line, upStation, downStation, distance, duration);
    }

    private void validate(int distance, int duration) {
        validateDistance(distance);
        validateDuration(duration);
    }

    private void validateDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new MinimumDistanceException(distance);
        }
    }

    private void validateDuration(int duration) {
        if (duration < MIN_DURATION) {
            throw new MinimumDistanceException(duration);
        }
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

}