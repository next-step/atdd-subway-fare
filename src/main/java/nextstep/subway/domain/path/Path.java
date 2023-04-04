package nextstep.subway.domain.path;

import java.util.List;
import lombok.Getter;
import nextstep.member.domain.MemberAgePolicy;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

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

    public int extractPrice(final MemberAgePolicy memberAgePolicy) {
        return sections.totalPrice(memberAgePolicy);
    }
}
