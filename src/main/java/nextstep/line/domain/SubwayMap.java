package nextstep.line.domain;

import nextstep.auth.principal.UserPrincipal;
import nextstep.exception.ShortPathSameStationException;
import nextstep.exception.StationNotExistException;
import nextstep.line.domain.fare.DistanceFarePolicies;
import nextstep.line.domain.path.*;
import nextstep.member.domain.Member;
import nextstep.station.domain.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubwayMap {

    private List<Line> lines;
    private List<ShortPathFinder> shortPathFinders;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
        this.shortPathFinders = List.of(new DistanceShortPathFinder(getStations(), getSections()), new DurationShortPathFinder(getStations(), getSections()));
    }

    public ShortPath findShortPath(ShortPathType type, Station startStation, Station endStation, Member member) {
        validateStation(startStation, endStation);
        return getShortPath(type, startStation, endStation, member);
    }

    public void validateStation(Station startStation, Station endStation) {
        if (isSameStation(startStation, endStation)) {
            throw new ShortPathSameStationException();
        }
        if (isNotExistStation(startStation, endStation)) {
            throw new StationNotExistException();
        }
    }

    private boolean isSameStation(Station startStation, Station endStation) {
        return startStation.equals(endStation);
    }

    private boolean isNotExistStation(Station startStation, Station endStation) {
        return !getStations().containsAll(List.of(startStation, endStation));
    }

    private ShortPath getShortPath(ShortPathType type, Station startStation, Station endStation, Member member) {
        return shortPathFinders.stream()
                .filter(shortPathFinder -> shortPathFinder.isSupport(type))
                .findAny()
                .map(shortPathFinder -> shortPathFinder.getShortPath(startStation, endStation, member))
                .orElse(null);
    }

    private List<Station> getStations() {
        Set<Station> stations = new HashSet<>();
        for (Line line : lines) {
            stations.addAll(line.getStations());
        }
        return new ArrayList<>(stations);
    }

    private List<Section> getSections() {
        List<Section> sections = new ArrayList<>();
        for (Line line : lines) {
            sections.addAll(line.getSections());
        }
        return sections;
    }

}
