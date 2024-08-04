package nextstep.subway.domain.entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import nextstep.subway.application.dto.response.PathResponse.StationDto;

@Getter
public class Path {

    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<StationDto> getStationDtoOfPath(Station target) {
        List<Station> stations = this.getStationsOfPath();
        if (!stations.isEmpty()) {
            Station sourceStation = stations.get(0);
            if (Objects.equals(sourceStation.getId(), target.getId())) {
                Collections.reverse(stations);
            }
        }
        return stations.stream()
            .map(StationDto::new)
            .collect(Collectors.toList());
    }

    public List<Station> getStationsOfPath() {
        return this.sections.getSortedStationsByUpDirection(true);
    }


    public long getDistance() {
        return this.sections.getDistance();
    }

    public long getDuration() {
        return this.sections.getDuration();
    }
}
