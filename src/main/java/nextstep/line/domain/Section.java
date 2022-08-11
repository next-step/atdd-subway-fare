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
    private int duration;

    protected Section() {
    }

    public Section(Long upStationId, Long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Section combine(Section anotherSection) {
        if (!this.downStationId.equals(anotherSection.upStationId)) {
            throw new IllegalSectionOperationException(CANT_COMBINE);
        }

        return new Section(
                this.upStationId,
                anotherSection.downStationId,
                this.distance + anotherSection.distance,
                this.duration + anotherSection.duration
        );
    }

    public Section subtract(Section anotherSection) {
        int subtractedDistance = this.distance - anotherSection.distance;
        if (subtractedDistance < 1) {
            throw new IllegalSectionOperationException(INVALID_DISTANCE);
        }

        int subtractedDuration = this.duration - anotherSection.duration;
        if (subtractedDuration < 1) {
            throw new IllegalSectionOperationException(INVALID_DURATION);
        }

        if (isSameUpStation(anotherSection.getUpStationId())) {
            return new Section(
                    anotherSection.downStationId,
                    this.downStationId,
                    subtractedDistance,
                    subtractedDuration
            );
        }

        if (isSameDownStation(anotherSection.getDownStationId())) {
            return new Section(
                    this.upStationId,
                    anotherSection.upStationId,
                    subtractedDistance,
                    subtractedDuration
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

    public Section flip() {
        return new Section(downStationId, upStationId, distance, duration);
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

    public int getDuration() {
        return duration;
    }
}