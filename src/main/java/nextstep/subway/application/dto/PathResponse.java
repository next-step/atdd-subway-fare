package nextstep.subway.application.dto;

import nextstep.subway.ui.controller.PathType;

import java.util.List;

public class PathResponse {
	private List<StationResponse> stations;
	private PathType type;
	private int distance;
	private int duration;
	private int fare;

	public PathResponse() {
	}

	public PathResponse(List<StationResponse> stations, PathType type, int distance, int duration, int fare) {
		this.stations = stations;
		this.type = type;
		this.distance = distance;
		this.duration = duration;
		this.fare = fare;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public PathType getType() {
		return type;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	public int getFare() {
		return fare;
	}
}
