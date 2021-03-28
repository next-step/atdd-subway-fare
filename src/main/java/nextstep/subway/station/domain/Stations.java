package nextstep.subway.station.domain;

import java.util.List;

public class Stations {

    // TODO : 보완
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
