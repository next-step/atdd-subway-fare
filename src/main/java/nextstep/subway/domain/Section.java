package nextstep.subway.domain;

import nextstep.subway.domain.exception.SectionCreateException;
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

    private int duration;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        validateAddSection(line, upStation, downStation, distance, duration);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
        this.duration = duration;
    }

    private void validateAddSection(
            final Line line,
            final Station upStation,
            final Station downStation,
            final int distance,
            final int duration
    ) {
        if (line == null || upStation == null || downStation == null || distance == 0 || duration == 0) {
            throw new SectionCreateException();
        }
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
        return distance.getValue();
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean hasDuplicateSection(Station upStation, Station downStation) {
        return (this.upStation.equals(upStation) && this.downStation.equals(downStation))
                || (this.upStation.equals(downStation) && this.downStation.equals(upStation));
    }

    public Section replaceDownStationWithUpStation(final Section section) {
        return createSectionBetweenExistSection(this.getUpStation(), section.getUpStation(), section);
    }

    public Section replaceUpStationWithDownStation(final Section section) {
        return createSectionBetweenExistSection(section.getDownStation(), this.getDownStation(), section);
    }

    private Section createSectionBetweenExistSection(
            final Station upStation,
            final Station downStation,
            final Section section
    ) {
        return new Section(
                this.getLine(),
                upStation,
                downStation,
                this.getDistance() - section.getDistance(),
                this.getDuration() - section.getDuration()
        );
    }
}
