package subway.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

@Getter
@Builder
public class PathFareCalculationInfo {
    private long fare;
    private Station sourceStation;
    private Station targetStation;
    private List<Section> sections;

    public PathFareCalculationInfo withUpdatedFare(long updatedFare) {
        return PathFareCalculationInfo.builder()
                .fare(updatedFare)
                .sourceStation(this.sourceStation)
                .targetStation(this.targetStation)
                .sections(this.sections)
                .build();
    }
}
