package nextstep.subway.applicaion.dto;


import nextstep.subway.domain.PathType;

public class FindPathRequest {
	private final Long source;
	private final Long target;
	private final PathType type;

	public FindPathRequest(Long source, Long target, PathType type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}

	public PathType getType() {
		return type;
	}
}
