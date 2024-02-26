package nextstep.subway.line.domain;

import nextstep.subway.line.section.domain.ApplyValues;
import nextstep.subway.line.section.domain.Section;
import nextstep.subway.line.section.domain.Sections;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Sections sections;
    @Column(nullable = false)
    private Long distance;
    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Long surcharge;

    protected Line() {
    }

    public Line(String name,
                String color,
                Station upStation,
                Station downStation,
                Long distance,
                Long duration,
                Long surcharge) {
        this.name = name;
        this.color = color;
        this.sections = createSections(upStation, downStation, distance, duration);
        this.distance = distance;
        this.duration = duration;
        this.surcharge = surcharge;
    }

    public Line(String name,
                String color,
                Station upStation,
                Station downStation,
                Long distance,
                Long duration) {
        this(name, color, upStation, downStation, distance, duration, 0L);
    }

    private static Sections createSections(Station upStation,
                                           Station downStation,
                                           Long distance,
                                           Long duration) {
        List<Section> list = new ArrayList<>();
        list.add(new Section(upStation, downStation, distance, duration));
        return Sections.from(list);
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

    public Sections getSections() {
        return sections;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getSurcharge() {
        return surcharge;
    }

    public void update(String name,
                       String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        ApplyValues applyValues = this.sections.add(section);
        applyValues.validAdd(this.distance, section.distance());
        this.distance += applyValues.applyDistance();
        this.duration += applyValues.applyDuration();
    }

    public void deleteSection(Station station) {
        ApplyValues applyValues = this.sections.delete(station);
        this.distance -= applyValues.applyDistance();
        this.duration -= applyValues.applyDuration();
    }

    public boolean existStation(Station station) {
        return sections.existStation(station);
    }

    public Stations getStations() {
        return this.sections.stations();
    }

    public Long calculateValue(Station source,
                               Station target,
                               PathType type) {
        return sections.calculateValue(source, target, type);
    }

    public boolean existSection(Station source,
                                Station target) {
        return sections.existSection(source, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections) && Objects.equals(distance, line.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections, distance);
    }
}
