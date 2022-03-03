package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

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
    private SectionInfo sectionInfo;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sectionInfo = new SectionInfo(distance, duration);
    }

    public Section(Line line, Station upStation, Station downStation, SectionInfo sectionInfo) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sectionInfo = sectionInfo;
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
        return sectionInfo.getDistance();
    }

    public int getBetweenDistance(int distance) {
        return sectionInfo.getBetweenDistance(distance);
    }

    public int getDuration() {
        return sectionInfo.getDuration();
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

    @Embeddable
    public static class SectionInfo {
        private int distance;
        private int duration;

        public SectionInfo() {
        }

        public SectionInfo(int distance, int duration) {
            this.distance = distance;
            this.duration = duration;
        }

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

        public int getBetweenDistance(int distance) {
            return this.distance - distance;
        }
    }

}