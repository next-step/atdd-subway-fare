package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;
    private int basicFare;
    private int lineOverFare;
    private int distanceOverFare;
    private int memberDiscountFare;

    public PathResponse(List<StationResponse> stations, int distance, int duration,
                        int fare, int basicFare, int lineOverFare, int distanceOverFare, int memberDiscountFare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.basicFare = basicFare;
        this.lineOverFare = lineOverFare;
        this.distanceOverFare = distanceOverFare;
        this.memberDiscountFare = memberDiscountFare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();
        int basicFare = 0;
        int lineOverFare = 0;
        int distanceOverFare = 0;
        int memberDiscountFare = 0;

        return new PathResponse(stations, distance, duration,
                fare, basicFare, lineOverFare, distanceOverFare, memberDiscountFare);
    }

    public List<StationResponse> getStations() {
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

    public int getBasicFare() {
        return basicFare;
    }

    public int getLineOverFare() {
        return lineOverFare;
    }

    public int getDistanceOverFare() {
        return distanceOverFare;
    }

    public int getMemberDiscountFare() {
        return memberDiscountFare;
    }
}
