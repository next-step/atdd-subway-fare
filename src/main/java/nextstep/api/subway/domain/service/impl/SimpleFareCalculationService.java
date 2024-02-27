package nextstep.api.subway.domain.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.outport.FareInfo;
import nextstep.api.subway.domain.operators.FareCalculator;
import nextstep.api.subway.domain.service.FareCalculationService;

/**
 * @author : Rene Choi
 * @since : 2024/02/27
 */
@Service
@RequiredArgsConstructor
public class SimpleFareCalculationService implements FareCalculationService {

	private final FareCalculator fareCalculator;

	@Override
	public FareInfo calculate(Long distance) {
		return FareInfo.of(fareCalculator.calculateFare(distance));
	}
}
