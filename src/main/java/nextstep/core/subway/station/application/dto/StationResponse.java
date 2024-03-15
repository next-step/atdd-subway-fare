package nextstep.core.subway.station.application.dto;

import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;

import java.util.*;

public class StationResponse {

    private final Long id;

    private final String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse toResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    public static List<StationResponse> toResponses(List<Section> sections) {
        Set<StationResponse> stationResponses = new LinkedHashSet<>();

        sections.forEach(section -> {
            stationResponses.add(toResponse(section.getUpStation()));
            stationResponses.add(toResponse(section.getDownStation()));
        });

        return new ArrayList<>(stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationResponse that = (StationResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
