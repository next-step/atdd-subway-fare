package nextstep.subway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

@Builder
@Getter
@AllArgsConstructor
public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Integer duration;

    public Section toSection(Line line, Station upStation, Station downStation) {
        return Section.builder()
            .line(line)
            .upStation(upStation)
            .downStation(downStation)
            .distance(this.distance)
            .duration(this.duration)
            .build();
    }

}
