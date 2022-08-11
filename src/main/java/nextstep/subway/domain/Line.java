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

    @Embedded
    private Fare fare = new Fare();

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, Fare.from(0));
    }

    public Line(String name, String color, Fare fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
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

    public int getFare() {
        return fare.fare();
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

    public void addSection(SectionBuilder sectionBuilder) {
        sections.add(Section.of(this, sectionBuilder.upStation, sectionBuilder.downStation, sectionBuilder.distance, sectionBuilder.duration));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public static class SectionBuilder {
        private Station upStation;
        private Station downStation;
        private Distance distance;
        private Duration duration;

        public SectionBuilder() { }

        public SectionBuilder(SectionBuilder sectionBuilder) {
            this.upStation = sectionBuilder.upStation;
            this.downStation = sectionBuilder.downStation;
            this.distance = sectionBuilder.distance;
            this.duration = sectionBuilder.duration;
        }

        public SectionBuilder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public SectionBuilder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public SectionBuilder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public SectionBuilder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public SectionBuilder build() {
            return new SectionBuilder(this);
        }
    }
}
