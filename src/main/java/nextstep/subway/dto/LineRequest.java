package nextstep.subway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

@Builder
@Getter
@AllArgsConstructor
public class LineRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public Line toLine(Station upStation, Station downStation) {
        return Line.builder()
            .name(this.name)
            .color(this.color)
            .distance(this.distance)
            .upStation(upStation)
            .downStation(downStation)
            .build();
    }

}
