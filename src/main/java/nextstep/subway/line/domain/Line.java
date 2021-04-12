package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int addFare = 0;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int addFare) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
    }

    public Line(String name, String color, int addFare, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.addFare = line.getAddFare();
        this.startTime = line.getStartTime();
        this.endTime = line.getEndTime();
        this.intervalTime = line.getIntervalTime();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAddFare() {
        return addFare;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.addSection(new Section(this, upStation, downStation, distance, duration));
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }

    public int getAddTimeFromUpStation(Station station) {
        return sections.getAddTimeFromUpStation(station);
    }

    public int getAddTimeBetweenSourceAndTarget(Station source, Station target) {
        return sections.getAddTimeBetweenSourceAndTarget(source, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return addFare == line.addFare && intervalTime == line.intervalTime && Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(startTime, line.startTime) && Objects.equals(endTime, line.endTime) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, addFare, startTime, endTime, intervalTime, sections);
    }
}
