package nextstep.line.domain;

import nextstep.exception.SectionDistanceOverException;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

@Entity
public class Section extends DefaultWeightedEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private int duration;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isUp(Station station) {
        return upStation.equals(station);
    }

    public boolean isDown(Station station) {
        return downStation.equals(station);
    }

    public boolean isExistedStation(Station station) {
        return isUp(station) || isDown(station);
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

    public void decreaseDistance(Section section) {
        if (isOverDistance(section)) {
            throw new SectionDistanceOverException();
        }
        this.distance -= section.getDistance();
    }

    public void increaseDistance(Section section) {
        this.distance += section.getDistance();
    }

    private boolean isOverDistance(Section section) {
        return distance <= section.getDistance();
    }

    public void modifyUpStation(Station station) {
        this.upStation = station;
    }

    public void modifyDownStation(Station station) {
        this.downStation = station;
    }

}
