package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.station.dto.StationResponse;

import java.util.List;

public class FarePathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;
    private int fare;

    public FarePathResponse() {
    }

    public FarePathResponse(List<StationResponse> stations, int duration, int distance, int fare) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
