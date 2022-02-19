package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

import nextstep.subway.domain.map.SubwayDispatchTime;

@Entity
public class Section extends DefaultWeightedEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column(name = "distance", nullable = false)
    private int distance;

    @Column(name = "duration", nullable = false)
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

    public int getAdditionalFare() {
        return line.getAdditionalFare();
    }

    public SubwayDispatchTime dispatchTime() {
        return line.dispatchTime(upStation);
    }
}
