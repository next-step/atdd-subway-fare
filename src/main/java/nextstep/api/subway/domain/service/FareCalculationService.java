package nextstep.api.subway.domain.service;

import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.subway.domain.dto.outport.FareInfo;
import nextstep.api.subway.domain.dto.outport.PathInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/27
 */
public interface FareCalculationService {
	FareInfo calculate(PathInfo pathInfo);
	FareInfo calculate(PathInfo pathInfo, UserPrincipal userPrincipal);
}
