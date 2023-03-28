package nextstep.subway.domain.Fare.policy;

import java.util.List;
import java.util.function.ToIntBiFunction;

public enum LineFarePolicy {
	MAX_LINE_FARE(Math::max);

	private final ToIntBiFunction<Integer, Integer> calculate;

	LineFarePolicy(ToIntBiFunction<Integer, Integer> calculate) {
		this.calculate = calculate;
	}

	public static int calculateByMaxLineFare(List<Integer> fares) {
		return fares.parallelStream()
			.reduce(0, MAX_LINE_FARE::getLineFare);
	}

	private int getLineFare(int fare1, int fare2) {
		return calculate.applyAsInt(fare1, fare2);
	}
}
