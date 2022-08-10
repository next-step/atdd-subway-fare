package nextstep.line.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.line.domain.Section;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SectionRequest implements EntitySupplier<Section> {
    @NotNull
    @Min(1)
    private Long upStationId;
    @NotNull
    @Min(1)
    private Long downStationId;
    @Min(1)
    private int distance;

    public Long getUpStationId() {
        return upStationId;
    }

    @Override
    public Section toEntity() {
        return new Section(upStationId, downStationId, distance);
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
