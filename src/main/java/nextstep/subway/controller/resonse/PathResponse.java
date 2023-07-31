package nextstep.subway.controller.resonse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stationResponses;
    private long distance;
    private int duration;

    public PathResponse(List<StationResponse> stationResponses, long distance, int duration) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.duration = duration;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public long getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
