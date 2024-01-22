package nextstep.subway.cucumber.steps.dto;

import java.util.Arrays;

public class PathStepResponse {

    private final String[] stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public PathStepResponse(final String[] stations, final int distance, final int duration, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public String[] getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return "PathStepResponse{" +
            "stations=" + Arrays.toString(stations) +
            ", distance=" + distance +
            ", duration=" + duration +
            ", fare=" + fare +
            '}';
    }
}
