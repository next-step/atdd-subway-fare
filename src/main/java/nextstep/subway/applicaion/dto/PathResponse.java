package nextstep.subway.applicaion.dto;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fee;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fee = fee;
    }

    public static PathResponse of(Path path, Member member) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fee = path.getFee(member);

        return new PathResponse(stations, distance, duration, fee);
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

    public int getFee() {
        return fee;
    }
}
