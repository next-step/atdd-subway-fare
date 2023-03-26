package nextstep.subway.domain.Fare;

import java.util.List;
import java.util.Optional;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Sections;
import nextstep.subway.exception.NegativeDistanceException;

public class Fare {
	private Member member;
	private Sections sections;
	private int value;

	private Fare(Member member, Sections sections) {
		this.member = member;
		this.sections = sections;
		this.value = 0;
	}

	public static Fare of(Member member, Sections sections) {
		if (!sections.isPositiveTotalDistance()) {
			throw new NegativeDistanceException();
		}

		return new Fare(member, sections);
	}

	public Sections getSections() {
		return sections;
	}

	public List<Integer> extractLineFares() {
		return sections.getLineFares();
	}

	public int extractTotalDistance() {
		return sections.totalDistance();
	}

	public Optional<Member> getMember() {
		return Optional.ofNullable(member);
	}

	public boolean isMemberPresent() {
		return getMember().isPresent();
	}

	public int applyMemberFarePolicy() {
		if (isMemberPresent()) {
			return this.member.applyFarePolicy(this.value);
		}
		return 0;
	}

	public int getValue() {
		return value;
	}

	public void addValue(int additionalValue) {
		this.value += additionalValue;
	}
}
