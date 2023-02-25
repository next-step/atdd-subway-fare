package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
	private int duration;
	private int charge;

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
		int duration = path.extractDuration();
		int charge = path.extractCharge();

        return new PathResponse(stations, distance, duration, charge);
    }
}
