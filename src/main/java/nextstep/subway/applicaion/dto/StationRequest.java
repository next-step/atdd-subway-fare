package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }

    public Station toEntity() {
        return new Station(name);
    }
}
