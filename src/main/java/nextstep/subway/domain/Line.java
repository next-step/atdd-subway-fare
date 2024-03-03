package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    private int extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int duration, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public Line(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }


    public List<Station> getStations() {
        return sections.getStations();
    }

    public void removeSection(Station upStation, Station downStation) {
        sections.remove(upStation, downStation);
    }

    public void removeStation(Station stationToDelete) {
      sections.removeStation(stationToDelete);
    }
}
