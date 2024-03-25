package nextstep.subway.domain.fare;

import nextstep.subway.domain.entity.Section;

import java.util.List;
import java.util.NoSuchElementException;

public class LinePolicy implements FarePolicy {
	private final List<Section> sections;

	public LinePolicy(List<Section> sections) {
		this.sections = sections;
	}

	@Override
	public int applyAdditionalFare(int fare) {
		return fare +
				sections.stream()
				.mapToInt(Section::getLineFee)
				.max()
				.orElseThrow(NoSuchElementException::new);
	}

	@Override
	public int applyDiscountFare(int fare) {
		return fare;
	}

	@Override
	public double applyDiscountPercent(int fare) {
		return fare;
	}
}
