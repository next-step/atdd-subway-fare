package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.ui.controller.PathType;

import java.util.List;

public class PathFinderFactory {
	public static PathFinder getPathFinder(List<Section> sections, PathType type) {
		switch (type) {
			case DISTANCE:
				return new PathByDistanceFinder(sections);
			case DURATION:
				return new PathByDurationFinder(sections);
			default:
				throw new IllegalArgumentException("경로 조회 조건이 올바르지 않습니다.");
		}
	}
}
