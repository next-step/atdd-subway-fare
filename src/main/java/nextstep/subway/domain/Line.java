package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    public static final int DEFALUT_SURCHARGE = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int surCharge;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.surCharge = DEFALUT_SURCHARGE;
    }

    public Line(String name, String color, int surCharge) {
        this.name = name;
        this.color = color;
        this.surCharge = surCharge;
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

    public int getSurCharge() {
        return surCharge;
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
}
