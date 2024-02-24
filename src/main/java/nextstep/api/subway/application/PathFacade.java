package nextstep.api.subway.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.common.type.PathType;
import nextstep.api.subway.domain.service.PathService;
import nextstep.api.subway.interfaces.dto.response.PathResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/24
 */
@Component
@RequiredArgsConstructor
public class PathFacade {
	private final PathService pathService;

	public PathResponse findPath(Long source, Long target, PathType type) {

		if(PathType.isDistance(type)){
			return pathService.findShortestPath(source, target);
		}
		return pathService.findMinimumDurationPath(source,target);

	}
}
