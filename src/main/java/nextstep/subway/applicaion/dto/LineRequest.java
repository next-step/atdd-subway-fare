package nextstep.subway.applicaion.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public boolean hasSectionInfo() {
        return Objects.nonNull(upStationId)
                && Objects.nonNull(downStationId)
                && distance > 0
                && duration > 0;
    }
}
