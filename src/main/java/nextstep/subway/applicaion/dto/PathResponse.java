package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;
    private int extraCharge;
    private int discount;
    private int totalFare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare, int extraCharge, int discount, int totalFare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.extraCharge = extraCharge;
        this.discount = discount;
        this.totalFare = totalFare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.fare();
        int extraCharge = path.extraCharge();
        int discount = path.discount();
        int totalFare = path.totalFare();

        return new PathResponse(stations, distance, duration, fare, extraCharge, discount, totalFare);
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

    public int getExtraCharge() {
        return extraCharge;
    }

    public int getDiscount() {
        return discount;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
