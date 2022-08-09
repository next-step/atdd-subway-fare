package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Section extends DefaultWeightedEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    @Embedded
    private Duration duration;

    protected Section() {}

    public Section(Line line, Station upStation, Station downStation, Distance distance, Duration duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this(line, upStation, downStation, new Distance(distance), new Duration(duration));
    }

    public Section(Station upStation, Station downStation, int distance, int duration) {
        this(null, upStation, downStation, distance, duration);
    }

    public Section(Line line, Section section) {
        this(line, section.upStation, section.downStation, section.distance, section.duration);
    }

    public static SectionBuilder builder() {
        return new SectionBuilder();
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.value();
    }

    public int getDuration() {
        return duration.value();
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation == station;
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation == station;
    }

    public boolean hasDuplicateSection(Station upStation, Station downStation) {
        return (this.upStation == upStation && this.downStation == downStation)
                || (this.upStation == downStation && this.downStation == upStation);
    }

    public Section changeUpStation(Section section) {
        this.line = section.line;
        this.downStation = section.upStation;
        this.distance.minus(section.distance);
        this.duration.minus(section.duration);
        return this;
    }

    public Section changeDownStation(Section section) {
        this.line = section.line;
        this.upStation = section.downStation;
        this.distance.minus(section.distance);
        this.duration.minus(section.duration);
        return this;
    }

    public Section mergeSection(Section section) {
        this.upStation = section.getUpStation();
        this.distance.plus(section.distance);
        this.duration.plus(section.duration);
        return this;
    }

    public static class SectionBuilder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int duration;

        public SectionBuilder line(Line line) {
            this.line = line;
            return this;
        }

        public SectionBuilder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public SectionBuilder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public SectionBuilder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public SectionBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Section build() {
            Section section = new Section();

            if (this.line != null) {
                section.line = this.line;
            }

            if (this.upStation != null) {
                section.upStation = this.upStation;
            }

            if (this.downStation != null) {
                section.downStation = this.downStation;
            }

            section.distance = new Distance(this.distance);
            section.duration = new Duration(this.duration);

            return section;
        }
    }
}