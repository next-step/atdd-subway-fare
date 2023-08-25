package nextstep.subway.dto;

import nextstep.subway.domain.entity.Station;

public class StationResponse {
    private Long id;

    public StationResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
