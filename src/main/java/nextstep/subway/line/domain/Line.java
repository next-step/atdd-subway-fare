package nextstep.subway.line.domain;


import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Embedded
    private Sections sections = new Sections();
    @Column(nullable = false)
    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int duration, int additionalFare) {
        this(null, name, color, upStation, downStation, distance, duration, additionalFare);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance, int duration, int additionalFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        addSection(new Section(this, upStation, downStation, distance, duration));
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void deleteSection(Long stationId) {
        sections.deleteSection(stationId);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                ", additionalFare=" + additionalFare +
                '}';
    }
}
