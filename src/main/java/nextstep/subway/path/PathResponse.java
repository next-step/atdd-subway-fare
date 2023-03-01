package nextstep.subway.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.applicaion.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PathResponse {
	private List<StationResponse> stations;
	private int distance;
	private int duration;
	private int fare;

	public static PathResponse of(Path path, int calculationFare) {
		List<StationResponse> stations = path.getStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		int distance = path.extractDistance();
		int duration = path.extractDuration();
		int fare = calculationFare;

		return new PathResponse(stations, distance, duration, fare);
	}
}
