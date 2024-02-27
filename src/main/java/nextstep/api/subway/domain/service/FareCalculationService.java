package nextstep.api.subway.domain.service;

import nextstep.api.subway.domain.dto.outport.FareInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/27
 */
public interface FareCalculationService {
	FareInfo calculate(Long distance);
}
