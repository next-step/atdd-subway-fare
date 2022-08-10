package nextstep.line.domain;

import nextstep.line.domain.exception.IllegalSectionOperationException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static nextstep.line.domain.exception.IllegalSectionOperationException.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section combine(Section anotherSection) {
        if (!this.downStationId.equals(anotherSection.upStationId)) {
            throw new IllegalSectionOperationException(CANT_COMBINE);
        }

        return new Section(
                this.upStationId,
                anotherSection.downStationId,
                this.distance + anotherSection.distance
        );
    }

    public Section subtract(Section anotherSection) {
        int subtractedDistance = this.distance - anotherSection.distance;
        if (subtractedDistance < 1) {
            throw new IllegalSectionOperationException(INVALID_DISTANCE);
        }

        if (isSameUpStation(anotherSection.getUpStationId())) {
            return new Section(
                    anotherSection.downStationId,
                    this.downStationId,
                    subtractedDistance
            );
        }

        if (isSameDownStation(anotherSection.getDownStationId())) {
            return new Section(
                    this.upStationId,
                    anotherSection.upStationId,
                    subtractedDistance
            );
        }

        throw new IllegalSectionOperationException(CANT_SUBTRACT);
    }

    public boolean isSameUpStation(Long upStationId) {
        return this.upStationId.equals(upStationId);
    }

    public boolean isSameDownStation(Long downStationId) {
        return this.downStationId.equals(downStationId);
    }

    public boolean hasDuplicateSection(Long upStationId, Long downStationId) {
        return (this.upStationId.equals(upStationId) && this.downStationId.equals(downStationId))
                || (this.downStationId.equals(upStationId) && this.upStationId.equals(downStationId));
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}