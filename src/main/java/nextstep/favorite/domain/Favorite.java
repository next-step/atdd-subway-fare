package nextstep.favorite.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    public Favorite() {
    }

    public Favorite(Long memberId, Station sourceStation, Station targetStation, List<Line> lines) {
        this(null, memberId, sourceStation, targetStation, lines);
    }

    public Favorite(Long id, Long memberId, Station sourceStation, Station targetStation, List<Line> lines) {
        isValidPath(lines, sourceStation, targetStation);

        this.id = id;
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void isValidPath(List<Line> lines, Station sourceStation, Station targetStation) {
        new Path(lines).isConnected(sourceStation, targetStation);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
