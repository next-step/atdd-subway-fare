package nextstep.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.Path;
import nextstep.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathResponse {

    private List<Station> stations;
    private Long distance;
    private Long duration;

    public static PathResponse createPathResponse(Path path){
        PathResponse pathResponse = new PathResponse();
        pathResponse.stations = path.getStations();
        pathResponse.distance = path.extractDistance();
        pathResponse.duration = path.extractDuration();

        return pathResponse;
    }
}
