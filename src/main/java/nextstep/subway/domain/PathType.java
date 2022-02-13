package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
	DISTANCE(Section::getDistance),
	DURATION(Section::getDuration);

	private final Function<Section, Integer> weightFunction;

	PathType(Function<Section, Integer> weightFunction) {
		this.weightFunction = weightFunction;
	}

	public int getWeight(Section section) {
		return this.weightFunction.apply(section);
	}
}
