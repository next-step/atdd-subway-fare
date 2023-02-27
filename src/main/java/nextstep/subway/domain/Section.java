package nextstep.subway.domain;

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

    public static Section of(Section upSection, Section downSection) {
        return new Section(
                upSection.getLine(),
                downSection.getUpStation(),
                upSection.getDownStation(),
                upSection.getDistance() + downSection.getDistance(),
                upSection.getDuration() + downSection.getDuration()
        );
    }

    public static Section createFrontSectionOf(Section oldSection, Section newSection) {
        return new Section(
                newSection.getLine(),
                oldSection.getUpStation(),
                newSection.getUpStation(),
                oldSection.getDistance() - newSection.getDistance(),
                oldSection.getDuration() - newSection.getDuration()
        );
    }

    public static Section createBackSectionOf(Section oldSection, Section newSection) {
        return new Section(
                newSection.getLine(),
                newSection.getDownStation(),
                oldSection.getDownStation(),
                oldSection.getDistance() - newSection.getDistance(),
                oldSection.getDuration() - newSection.getDuration()
        );
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

    public boolean hasDuplicateSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        return (this.upStation.equals(upStation) && this.downStation.equals(downStation))
                || (this.upStation.equals(downStation) && this.downStation.equals(upStation));
    }

    public Section reverse() {
        return new Section(
                this.getLine(),
                this.getDownStation(),
                this.getUpStation(),
                this.getDistance(),
                this.getDuration()
        );
    }
}
