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
    private long fare;
    private Station sourceStation;
    private Station targetStation;
    private List<Section> wholeSections;
    private List<Section> searchedSections;
    private Member member;

    public PathFareCalculationInfo withUpdatedFare(long updatedFare) {
        return PathFareCalculationInfo.builder()
                .fare(updatedFare)
                .sourceStation(this.sourceStation)
                .targetStation(this.targetStation)
                .wholeSections(this.wholeSections)
                .searchedSections(this.searchedSections)
                .build();
    }

    public static PathFareCalculationInfo from(PathFinderRequest request, List<Section> sections) {
        return PathFareCalculationInfo.builder()
                .sourceStation(request.getSourceStation())
                .targetStation(request.getTargetStation())
                .wholeSections(request.getSections())
                .searchedSections(sections)
                .member(request.getMember())
                .build();
    }
}
