package subway.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Section;
import subway.member.domain.Member;
import subway.station.domain.Station;

import java.util.List;

@Getter
@Builder
public class PathFareCalculationInfo {
    private static final long BASE_FARE = 1250L;
    private long fare;
    private Station sourceStation;
    private Station targetStation;
    private List<Section> sections;
    private long memberAge;


    public static PathFareCalculationInfo from(PathFinderRequest request) {
        return PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .sourceStation(request.getSourceStation())
                .targetStation(request.getTargetStation())
                .sections(request.getSections())
                .memberAge(request.getMemberAge())
                .build();
    }

    public PathFareCalculationInfo withUpdatedFare(long updatedFare) {
        return PathFareCalculationInfo.builder()
                .fare(updatedFare)
                .sourceStation(this.sourceStation)
                .targetStation(this.targetStation)
                .sections(this.sections)
                .memberAge(this.memberAge)
                .build();
    }

    public PathFareCalculationInfo withUpdatedSections(List<Section> sections) {
        return PathFareCalculationInfo.builder()
                .fare(this.fare)
                .sourceStation(this.sourceStation)
                .targetStation(this.targetStation)
                .sections(sections)
                .memberAge(this.memberAge)
                .build();
    }
}
