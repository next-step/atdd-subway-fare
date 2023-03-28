package nextstep.subway.domain.Fare.handler;

import nextstep.subway.domain.Fare.Fare;

public interface FareHandler {
	void setHandler(FareHandler nextHandler);

	void calculate(Fare fare);
}
