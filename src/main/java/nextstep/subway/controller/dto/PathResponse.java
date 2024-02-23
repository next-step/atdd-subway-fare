package nextstep.subway.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@JsonInclude(NON_NULL)
@AllArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private Integer duration;

    public PathResponse() {
    }

    public static PathResponse distanceOf(List<StationResponse> stations, int distance) {
        return PathResponse.builder()
                .stations(stations)
                .distance(distance)
                .build();
    }

    public static PathResponse durationOf(List<StationResponse> stations, int duration) {
        return PathResponse.builder()
                .stations(stations)
                .duration(duration)
                .build();
    }

    public static PathResponse otherOf(List<StationResponse> stations) {
        return PathResponse.builder()
                .stations(stations)
                .duration(0)
                .build();
    }

}
