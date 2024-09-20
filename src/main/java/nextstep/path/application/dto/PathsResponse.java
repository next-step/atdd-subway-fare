package nextstep.path.application.dto;

import nextstep.fare.domain.Fare;
import nextstep.station.application.dto.StationResponse;

import java.util.List;

public class PathsResponse {
    private int distance;
    private int duration;
    private long fare;
    private List<StationResponse> stations;

    public PathsResponse() {
    }

    private PathsResponse(int distance, int duration, long fare, List<StationResponse> stations) {
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getFare() {
        return fare;
    }

    public void setFare(long fare) {
        this.fare = fare;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int distance;
        private int duration;
        private long fare;
        private List<StationResponse> stations;

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder fare(Fare fare) {
            this.fare = fare.getFare();
            return this;
        }

        public Builder stations(List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public PathsResponse build() {
            return new PathsResponse(distance, duration, fare, stations);
        }
    }
}
