package nextstep.path.domain.dto;


import nextstep.station.domain.Station;

import java.util.List;

public class PathsDto {

    private final int distance;
    private final int duration;
    private final int fare;
    private final List<Station> paths;

    public PathsDto(int distance, int duration, int fare, List<Station> paths) {
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.paths = paths;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public List<Station> getPaths() {
        return paths;
    }

    public int getFare() {
        return 0;
    }
}
