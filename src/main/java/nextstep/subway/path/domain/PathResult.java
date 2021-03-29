package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public class PathResult {
    private static final int BASE_FARE = 1250;
    public static final int NO_ADDITIONAL_FARE = 0;
    public static final int ADDITIONAL_FARE_PER_DISTANCE = 100;

    public static final int DISTANCE_TEN = 10;
    public static final int DISTANCE_FIFTY = 50;

    public static final int ONE = 1;
    public static final int PER_EIGHT = 8;
    public static final int PER_FIVE = 5;

    private Sections sections;
    private Stations stations;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
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
        return BASE_FARE + calculateAdditionalFare(getTotalDistance());
    }

    private int calculateAdditionalFare(int distance) {
        if(distance <= DISTANCE_TEN) {
            return NO_ADDITIONAL_FARE;
        }

        if(distance > DISTANCE_FIFTY) {
            return (int) ((Math.ceil((distance - ONE) / PER_EIGHT) + ONE) * ADDITIONAL_FARE_PER_DISTANCE);
        }

        return (int) ((Math.ceil((distance - ONE) / PER_FIVE) + ONE) * ADDITIONAL_FARE_PER_DISTANCE);
    }
}
