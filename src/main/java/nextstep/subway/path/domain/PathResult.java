package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PathResult {
    private static final String AGE = "age";

    private Sections sections;
    private Stations stations;
    private LoginMember loginMember;
    private Fare fare;

    public PathResult(Stations stations, Sections sections, LoginMember loginMember) {
        this.stations = stations;
        this.sections = sections;
        this.loginMember = loginMember;
        this.fare = new Fare();
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }

    public int getTotalFare() {
        List<FarePolicy> farePolicies = initializeFarePolicies();

        return fare.calculateFare(farePolicies);
    }

    private List<FarePolicy> initializeFarePolicies() {
        List<FarePolicy> farePolicies = new ArrayList<>();

        farePolicies.add(new DistanceFarePolicy(getTotalDistance()));
        farePolicies.add(new LineFarePolicy(getDistinctLines()));

        if(!ObjectUtils.isEmpty(loginMember)) {
            farePolicies.add(new UserFarePolicy(loginMember.getAge()));
        }


        return farePolicies;
    }

    private List<Line> getDistinctLines() {
        return sections.getDistinctLines();
    }
}
