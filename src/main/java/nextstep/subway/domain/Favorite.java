package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public boolean isCreatedBy(Long memberId) {
        return this.memberId == memberId;
    }
}
