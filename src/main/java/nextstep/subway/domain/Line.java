package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int additionalCharge;
    @Embedded
    private Sections sections = new Sections();

    protected Line() {/*no-op*/}

    public Line(String name, String color, int additionalCharge) {
        this.name = name;
        this.color = color;
        this.additionalCharge = additionalCharge;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public int getAdditionalCharge() {
        return additionalCharge;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, int additionalCharge) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        this.additionalCharge = additionalCharge;
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

}
