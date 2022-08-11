package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Path;
import support.ticket.TicketType;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(Path path, TicketType ticketType) {
        List<StationResponse> stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = FareCalculator.calculateFare(distance) + path.extractAdditionalFare();
        int finalFare = ticketType.calculateDiscountedFare(fare);

        return new PathResponse(stations, distance, duration, finalFare);
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
}
