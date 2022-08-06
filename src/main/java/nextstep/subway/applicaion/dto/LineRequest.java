package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

import static nextstep.utils.NumberUtils.*;

@Getter
@NoArgsConstructor
public class LineRequest {
    private String name;

    private String color;

    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
