package nextstep.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Path;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PathResponse {

    List<StationResponse> stations;

    Long distance;

    Integer duration;

    Integer fare;

    public static PathResponse from(Path path) {
        return PathResponse.builder()
            .stations(path.getSections().getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()))
            .distance(path.getSections().getTotalDistance())
            .duration(path.getSections().getTotalDuration())
            .fare(path.getFare())
            .build();
    }

}
