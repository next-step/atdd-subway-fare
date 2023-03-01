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
    private Distance distance;

    @Embedded
    private Duration duration;

    public Section() {

    }

    private Section(Line line, Station upStation, Station downStation, Distance distance, Duration duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
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

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
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

    public Duration minusDuration(Duration duration) {
        return this.duration.minus(duration);
    }

    public Duration plusDuration(Duration duration) {
        return this.duration.plus(duration);
    }

    public Distance minusDistance(Distance distance) {
        return this.distance.minus(distance);
    }

    public Distance plusDistance(Distance distance) {
        return this.distance.plus(distance);
    }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private Distance distance;
        private Duration duration;

        public Builder() {
        }

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Section build() {
            if (line == null || upStation == null || downStation == null
                    || distance == null || duration == null) {
                throw new IllegalStateException("Section 생성 불가");
            }

            return new Section(line, upStation, downStation, distance, duration);
        }
    }
}
