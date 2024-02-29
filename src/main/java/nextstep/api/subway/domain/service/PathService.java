package nextstep.api.subway.domain.service;

import nextstep.api.subway.domain.dto.outport.PathInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
public interface PathService {
	PathInfo findShortestPath(Long source, Long target);
	PathInfo findMinimumDurationPath(Long source, Long target);
}
