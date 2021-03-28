package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.SectionNonExistException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static nextstep.subway.line.exception.LineExceptionMessage.EXCEPTION_MESSAGE_NOT_FOUND_SECTION;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);
        sections.addSection(section);
    }

    // TODO : 보완
    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.addSection(new Section(this, upStation, downStation, distance, duration));
    }

    public void removeSection(Station removeStation) {
        sections.removeSection(removeStation);
    }

    public Section findSection(Station upStation, Station downStation) {
        return sections.getSections().stream()
                .filter(section -> section.getUpStation().equals(upStation) && section.getDownStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new SectionNonExistException(EXCEPTION_MESSAGE_NOT_FOUND_SECTION));
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
