package nextstep.subway.controller.resonse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stationResponses;
    private long distance;
    private int duration;
    private int fare;

    public PathResponse(List<StationResponse> stationResponses, long distance, int duration, int fare) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public PathResponse() {
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

    public int getFare() {
        return fare;
    }
}
