package nextstep.line.domain;

import nextstep.exception.ShortPathSameStationException;
import nextstep.exception.StationNotExistException;
import nextstep.line.domain.fare.DistanceFarePolicies;
import nextstep.line.domain.path.*;
import nextstep.station.domain.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubwayMap {

    private List<Line> lines;
    private List<ShortPathFinder> shortPathFinders;
    private DistanceFarePolicies distanceFarePolicies;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
        this.shortPathFinders = List.of(new DistanceShortPathFinder(getStations(), getSections()), new DurationShortPathFinder(getStations(), getSections()));
        this.distanceFarePolicies = new DistanceFarePolicies();
    }

    public ShortPath findShortPath(ShortPathType type, Station startStation, Station endStation) {
        validateStation(startStation, endStation);
        return getShortPath(type, startStation, endStation);
    }

    public int getFare(ShortPath shortPath) {
        return distanceFarePolicies.getFare(shortPath.getDistance());
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

    private ShortPath getShortPath(ShortPathType type, Station startStation, Station endStation) {
        return shortPathFinders.stream()
                .filter(shortPathFinder -> shortPathFinder.isSupport(type))
                .findAny()
                .map(shortPathFinder -> shortPathFinder.getShortPath(startStation, endStation))
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
