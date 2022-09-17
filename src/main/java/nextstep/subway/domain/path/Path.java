package nextstep.subway.domain;

import lombok.Getter;
import support.auth.userdetails.UserDetails;

import java.util.List;

@Getter
public class Path {
    private Sections sections;
    private UserDetails user;

    private Path(Sections sections, UserDetails user) {
        this.user = user;
        this.sections = sections;
    }

    public static Path of(Sections sections, UserDetails user) {
        return new Path(sections, user);
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int lineExtraFare() {
        return sections.lineExtraFare();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
