package nextstep.subway.applicaion.dto.request;

import lombok.Getter;

@Getter
public class LineRequest {

    private final String name;

    private final String color;

    private final int additionalFare;

    private final Long upStationId;

    private final Long downStationId;

    private final int distance;

    private final int duration;

    public LineRequest(
            final String name, final String color, final int additionalFare, final Long upStationId,
            final Long downStationId, final int distance, final int duration
    ) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
