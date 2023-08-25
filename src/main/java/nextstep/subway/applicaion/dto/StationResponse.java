package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static StationResponse of(Station station) {
        return new StationResponse(station);
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse() { }

    public StationResponse(Station station) {
        id = station.getId();
        name = station.getName();
        createdTime = station.getCreatedTime();
        modifiedTime = station.getModifiedTime();
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getModifiedTime() { return modifiedTime; }
}