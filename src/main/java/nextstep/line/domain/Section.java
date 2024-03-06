package nextstep.line.domain;


import nextstep.path.domain.LineFare;
import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;
    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;
    private Integer distance;
    private Integer duration;
    @ManyToOne
    private Line line;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Integer distance, Integer duration, Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
        this.line = line;
    }


    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }



    public List<Section> divide(Section newSection) {
        Section next = new Section(newSection.getDownStation(), this.downStation, this.distance - newSection.distance, this.duration - newSection.getDuration(), line);
        this.downStation = newSection.getDownStation();
        this.distance = newSection.getDistance();
        this.duration = newSection.getDuration();
        return new LinkedList<>(List.of(this, next));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id)
                && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation)
                && Objects.equals(distance, section.distance)
                && Objects.equals(line, section.line)
                && Objects.equals(duration, section.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance, line, duration);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                ", duration=" + duration +
                '}';
    }

    public boolean equalSection(Section newSection) {
        return Objects.equals(upStation, newSection.getUpStation())
                && Objects.equals(downStation, newSection.getDownStation());
    }

    public void changeDownStation(Station toDown, int distance) {
        this.downStation = toDown;
        this.distance += distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public LineFare getLineFare() {
        return new LineFare(line.getId(), line.getExtraFare());
    }
}
