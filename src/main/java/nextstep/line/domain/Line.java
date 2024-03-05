package nextstep.line.domain;


import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.Collections;
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
    private Sections sections = new Sections();

    @Column(nullable = false)
    private long extraFare;

    protected Line() {
    }

    public Line(final String name, final String color, final long extraFare, final Section section) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        addSection(section);
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

    public List<Station> getStations() {
        return Collections.unmodifiableList(sections.getStations());
    }

    public int getDistance() {
        return sections.getDistance();
    }

    public int getDuration() {
        return sections.getDuration();
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changeColor(final String color) {
        this.color = color;
    }

    public void addSection(final Section section) {
        sections.connect(section);
    }

    public void removeSectionByStation(final Station station) {
        sections.disconnect(station);
    }

    public Sections getSections() {
        return sections;
    }

    public long getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final Line line = (Line) object;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
