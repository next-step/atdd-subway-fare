package nextstep.subway.domain.path;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.ui.controller.PathType;

import java.util.List;

public class SubwayMapFactory {
	public static PathFinder getSubwayMap(List<Section> sections, PathType type) {
		switch (type) {
			case DISTANCE:
				return new SubwayMapByDistance(sections);
			case DURATION:
				return new SubwayMapByDuration(sections);
			default:
				throw new IllegalArgumentException("경로 조회 조건이 올바르지 않습니다.");
		}
	}
}
