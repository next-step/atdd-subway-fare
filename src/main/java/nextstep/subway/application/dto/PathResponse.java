package nextstep.subway.application.dto;

import nextstep.subway.ui.controller.PathType;

import java.util.List;

public class PathResponse {
	private List<StationResponse> stations;
	private PathType type;
	private int weight;

	public PathResponse() {
	}

	public PathResponse(List<StationResponse> stations, PathType type, int weight) {
		this.stations = stations;
		this.type = type;
		this.weight = weight;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public PathType getType() {
		return type;
	}

	public int getWeight() {
		return weight;
	}
}
