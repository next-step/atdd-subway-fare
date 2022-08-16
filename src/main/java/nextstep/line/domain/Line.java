package nextstep.line.domain;

import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    private static final int DEFAULT_EXTRA_FARE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int extraFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(name, color, DEFAULT_EXTRA_FARE);
    }

    public Line(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public void update(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(upStation.getId(), downStation.getId(), distance, duration));
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void deleteSection(Station station) {
        sections.delete(station.getId());
    }

    public boolean containsAnyOf(List<Section> anotherSections) {
        return sections.containsAnyOf(anotherSections);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Long> getStations() {
        return sections.getStations();
    }
}
