package nextstep.subway.domain.path;

import java.util.List;

import nextstep.subway.domain.Line;

public enum PathBaseCode {
	DISTANCE("거리기준"),
	DURATION("시간기준");

	private String description;

	PathBaseCode(String description) {
		this.description = description;
	}

	public PathFinder getPathFinderClass(List<Line> line) {

		if (this == DISTANCE) {
			return new PathDistanceFinder(line);
		}
		return new PathDurationFinder(line);
	}
}
