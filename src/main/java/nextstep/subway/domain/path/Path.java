package nextstep.subway.domain.path;

import lombok.Getter;
import nextstep.member.domain.MemberAge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

import java.util.List;

@Getter
public class Path {
    private final Sections sections;

    public Path(final Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractPrice(final MemberAge memberAge) {
        return sections.totalPrice(memberAge);
    }
}
