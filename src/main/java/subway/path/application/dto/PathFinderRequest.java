package subway.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Section;
import subway.member.domain.Member;
import subway.station.domain.Station;

import java.util.List;

@Getter
@Builder
public class PathFinderRequest {
    private List<Section> sections;
    private Station sourceStation;
    private Station targetStation;
    private Member member; // TODO 비교 후 제거
//    private long memberAge;
}
