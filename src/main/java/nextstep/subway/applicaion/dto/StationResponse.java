package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDateTime(), station.getModifiedDateTime());
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private StationResponse() {
    }

    public StationResponse(Long id, String name, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
