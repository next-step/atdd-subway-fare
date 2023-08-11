package nextstep.line.domain;

import nextstep.station.domain.Station;

import java.util.List;

public abstract class ShortPathFinder {

    protected List<Station> stations;
    protected List<Section> sections;

    public ShortPathFinder(List<Station> stations, List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public abstract boolean isSupport(ShortPathType type);

    public abstract ShortPath getShortPath(Station source, Station target);

}
