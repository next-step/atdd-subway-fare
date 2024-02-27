package nextstep.api.subway.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.common.type.PathRequestType;
import nextstep.api.subway.domain.dto.outport.FareInfo;
import nextstep.api.subway.domain.dto.outport.PathInfo;
import nextstep.api.subway.domain.service.FareCalculationService;
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
	private final FareCalculationService fareCalculationService;

	public PathResponse findPath(Long source, Long target, PathRequestType type) {

		if(PathRequestType.isDistance(type)){
			PathInfo shortestPath = pathService.findShortestPath(source, target);
			FareInfo fareInfo = fareCalculationService.calculate(shortestPath.getDistance());
			return PathResponse.of(shortestPath, fareInfo);
		}
		PathInfo minimumDurationPath = pathService.findMinimumDurationPath(source, target);
		FareInfo fareInfo = fareCalculationService.calculate(minimumDurationPath.getDistance());
		return PathResponse.of(minimumDurationPath, fareInfo);
	}
}
