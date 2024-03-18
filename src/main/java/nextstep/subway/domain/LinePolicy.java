package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;

import java.util.List;
import java.util.NoSuchElementException;

public class LinePolicy implements FarePolicy {
	private final List<Section> sections;

	public LinePolicy(List<Section> sections) {
		this.sections = sections;
	}

	@Override
	public int getAdditionalFee() {
		return sections.stream()
				.mapToInt(Section::getLineFee)
				.max()
				.orElseThrow(NoSuchElementException::new);
	}

	@Override
	public int getDiscountFee() {
		return 0;
	}
}
