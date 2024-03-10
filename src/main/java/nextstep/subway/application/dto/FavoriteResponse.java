package nextstep.subway.application.dto;

import nextstep.subway.ui.controller.PathType;

public class FavoriteResponse {
	private Long id;
	private PathType pathType;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(Long id, PathType pathType, StationResponse source, StationResponse target) {
		this.id = id;
		this.pathType = pathType;
		this.source = source;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public StationResponse getSource() {
		return source;
	}

	public StationResponse getTarget() {
		return target;
	}

	public PathType getPathType() {
		return pathType;
	}
}
