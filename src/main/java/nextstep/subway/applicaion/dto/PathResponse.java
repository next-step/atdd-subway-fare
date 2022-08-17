package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;
    private int price;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare, int price) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.price = price;
    }

    public static PathResponse of(Path path, int age) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare(age);
        int price = path.maxPrice();

        return new PathResponse(stations, distance, duration, fare, price);
    }

    public static PathResponse of(Path path, Path distancePath, int age) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = distancePath.extractFare(age);
        int price = path.maxPrice();

        return new PathResponse(stations, distance, duration, fare, price);
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

    public int getPrice() {
        return price;
    }

}
