package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.domain.Path;

public class PathResponse {
	private List<StationResponse> stations;
	private int distance;
	private int duration;
	private long fee;

	public PathResponse(List<StationResponse> stations, int distance, int duration, long fee) {
		this.stations = stations;
		this.distance = distance;
		this.duration = duration;
		this.fee = fee;
	}

	public static PathResponse of(Path path, long fee) {
		List<StationResponse> stations = path.getStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		int distance = path.extractDistance();
		int duration = path.extractDuration();

		return new PathResponse(stations, distance, duration, fee);
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	public long getFee() {
		return fee;
	}
}
