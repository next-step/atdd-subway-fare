package nextstep.subway.domain;

import nextstep.subway.ui.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static nextstep.subway.ui.exception.ExceptionMessage.EXISTS_STATION_IN_SECTION;

@Entity
public class Section {
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
    private Distance distance;
    private int duration;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
        this.duration = duration;
    }

    public void updateAddLineBetweenSection(Section newSection) {
        if (isBetweenSection(newSection.getUpStation())) {
            validateDuplicationSection(newSection);
            this.upStation = newSection.getDownStation();
            this.distance = this.distance.subtract(newSection.getDistance());
            this.duration = this.duration - newSection.duration;
        }
    }

    public void updateRemoveLineBetweenSection(Section removeSection) {
        if (isBetweenSection(removeSection.getDownStation())) {
            this.upStation = removeSection.getUpStation();
            this.distance = this.distance.sum(removeSection.getDistance());
            this.duration = this.duration + removeSection.duration;
        }
    }

    private boolean isBetweenSection(Station station) {
        return this.upStation.equals(station);
    }

    private void validateDuplicationSection(Section newSection) {
        if (this.upStation.equals(newSection.upStation) && this.downStation.equals(newSection.getDownStation())) {
            throw new SectionException(EXISTS_STATION_IN_SECTION.getMsg());
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
        return distance.getDistance();
    }

    public int getDuration() {
        return duration;
    }
}