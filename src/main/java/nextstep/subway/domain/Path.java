package nextstep.subway.domain;

import java.util.List;

public class Path {
    private PathType pathType;
    private Sections sections;
    private boolean loginStaus = false;
    private int userAge = 0;

    public Path(PathType pathType, Sections sections) {
        this.pathType = pathType;
        this.sections = sections;
    }

    public void isLogin(int age) {
        this.loginStaus = true;
        this.userAge = age;
    }

    public Sections getSections() {
        return sections;
    }

    public PathType getPathType() {
        return pathType;
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

    public long extractTotalFare() {
        Fare fare = Fare.builder()
                .loginStatus(loginStaus)
                .age(userAge)
                .distance(sections.totalDistance())
                .overFareLine(sections.overFareLine())
                .build();

        return fare.getFare().longValue();
    }
}
