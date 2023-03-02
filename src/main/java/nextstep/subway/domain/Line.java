package nextstep.subway.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();
    @Embedded
    private Fare extraFare;

    protected Line() {}

    public Line(final String name, final String color, final Sections sections, final Fare extraFare) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.extraFare = extraFare;
    }

    public Line(final String name, final String color) {
        this(name, color, new Sections(), new Fare(BigDecimal.ZERO));
    }

    public Line(final String name, final String color, final BigDecimal fare) {
        this(name, color, new Sections(), new Fare(fare));
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public void update(final String name, final String color, final BigDecimal fare) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        if (fare != null) {
            this.extraFare = new Fare(fare);
        }
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

    public Fare getExtraFare() {
        return extraFare;
    }
}
