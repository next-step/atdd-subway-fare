package nextstep.subway.domain.Fare.handler;

import java.util.Objects;

import nextstep.subway.domain.Fare.Fare;

public class AgeFarePolicyHandler implements FareHandler {
	private FareHandler nextHandler;

	@Override
	public void setHandler(FareHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	@Override
	public void calculate(Fare fare) {
		fare.addValue(fare.applyMemberFarePolicy());

		if (Objects.nonNull(this.nextHandler)) {
			this.nextHandler.calculate(fare);
		}
	}
}
