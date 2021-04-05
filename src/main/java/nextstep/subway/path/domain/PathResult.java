package nextstep.subway.path.domain;

import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.CannotParseUserAgeException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PathResult {
    private static final String AGE = "age";

    private Sections sections;
    private Stations stations;
    private Fare fare;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
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

        return fare.getTotalFare(farePolicies);
    }

    private List<FarePolicy> initializeFarePolicies() {
        List<FarePolicy> farePolicies = new ArrayList<>();

        farePolicies.add(new DistanceFarePolicy(getTotalDistance()));
        farePolicies.add(new LineFarePolicy(getDistinctLines()));
        farePolicies.add(new UserFarePolicy(getUserAge()));

        return farePolicies;
    }

    private List<Line> getDistinctLines() {
        return sections.getDistinctLines();
    }

    private int getUserAge() {
        if(isAuthenticationExists()) {
            return parseUserAge();
        }
        return -1;
    }

    private boolean isAuthenticationExists() {
        return !ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication());
    }

    private int parseUserAge() {
        try{
            Map<String, String> principal = (Map) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Integer.parseInt(principal.get(AGE));
        } catch (Exception e) {
            throw new CannotParseUserAgeException();
        }
    }
}
