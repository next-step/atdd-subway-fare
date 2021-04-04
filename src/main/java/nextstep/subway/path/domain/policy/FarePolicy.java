package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;

public interface FarePolicy {
	 Fare getFare(int fare);
}