package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Integer extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, Integer extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public Integer getExtraFare() {
        return extraFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        if (getId() != null && line.id != null) {
            return getId().equals(line.id);
        }
        return Objects.equals(getName(), line.getName()) && Objects.equals(getColor(), line.getColor()) && Objects.equals(getExtraFare(), line.getExtraFare());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getColor(), getExtraFare());
    }
}
