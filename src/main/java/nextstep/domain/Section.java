package nextstep.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.subway.PathType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Section {
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

    private Long distance;
    private Long duration;

    @Builder
    public Section( Line line, Station upStation, Station downStation, Long distance,Long duration) {
        this.line = Objects.requireNonNull(line);
        this.upStation = Objects.requireNonNull(upStation);
        this.downStation = Objects.requireNonNull(downStation);
        this.distance = Objects.requireNonNull(distance);
        this.duration =Objects.requireNonNull(duration);
    }

    public Long getWeight(PathType type){
        if(type.equals(PathType.DISTANCE)){
            return distance;
        }
        if(type.equals(PathType.DURATION)){
            return duration;
        }
        else{
            throw new IllegalArgumentException("잘못된 경로조회 타입.");
        }
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation == station;
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation == station;
    }

}