package nextstep.line.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.line.domain.Line;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class LineRequest implements EntitySupplier<Line> {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Min(1)
    private Long upStationId;
    @Min(1)
    private Long downStationId;
    @Min(1)
    private Integer distance;
    @Min(1)
    private Integer duration;

    @Override
    public Line toEntity() {
        return new Line(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
