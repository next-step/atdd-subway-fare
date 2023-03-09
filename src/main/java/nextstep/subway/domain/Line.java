package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
    private static final int BASE_ADDITIONAL_FARE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private AdditionalFare additionalFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(final String name, final String color) {
        this(name, color, BASE_ADDITIONAL_FARE);
    }

    public Line(final String name, final String color, final int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = new AdditionalFare(additionalFare);
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

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSectionIfPossible(
            final Station upStation,
            final Station station,
            final int distance,
            final int duration
    ) {
        try {
            addSection(upStation, station, distance, duration);
        } catch (IllegalArgumentException exception) {
            // 예외가 발생하면 구간을 추가하지 않는다.
        }
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public AdditionalFare getAdditionalFare() {
        return additionalFare;
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }
}
