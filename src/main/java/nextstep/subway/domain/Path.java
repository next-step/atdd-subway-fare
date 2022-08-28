package nextstep.subway.domain;

import support.auth.userdetails.UserDetails;

import java.util.List;

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

    public int extractFare() {
        int maxAddFare = sections.sections().stream().mapToInt(section -> section.getLine().getAddFare()).filter(section -> section >= 0).max().orElse(0);
        return sections.totalFare() + maxAddFare;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
