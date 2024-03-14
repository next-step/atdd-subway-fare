package nextstep.section;

import nextstep.exception.SubwayException;
import nextstep.line.Line;
import nextstep.station.Station;

import javax.persistence.*;

@Entity
public class Section implements Comparable<Section> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Column
    private int distance;

    @Column
    private int duration;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public void separate(Section middleSection) {
        this.upStation = middleSection.getDownStation();
        validateSectionDistance(middleSection);
        this.distance -= middleSection.distance;
    }

    private void validateSectionDistance(Section middleSection) {
        if (this.distance <= middleSection.distance) {
            throw new SubwayException("중간 구간의 거리는 다음 구간보다 짧아야합니다.");
        }
    }

    public void join(Section downSection) {
        this.distance += downSection.distance;
        this.downStation = downSection.downStation;
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
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public int compareTo(final Section section) {
        if (this.equals(section)) {
            return 0;
        }
        if (this.downStation.equals(section.upStation)) {
            return -1;
        }
        return 1;
    }
}
