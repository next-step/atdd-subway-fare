package nextstep.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathResponse {

    private List<Station> stations;
    private int distance;

    public static PathResponse createPathResponse(List<Station> path,int distance){
        PathResponse pathResponse = new PathResponse();
        pathResponse.stations = path;
        pathResponse.distance = distance;

        return pathResponse;
    }
}
