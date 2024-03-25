package nextstep.subway.domain.fare;

public class AgePolicy implements FarePolicy {
	private final int age;

	public AgePolicy(int age) {
		this.age = age;
	}

	@Override
	public int applyAdditionalFare(int fare) {
		return fare;
	}

	@Override
	public int applyDiscountFare(int fare) {
		if(isChild(age) || isTeenager(age)) {
			return fare - 350;
		}

		return fare;
	}

	@Override
	public double applyDiscountPercent(int fare) {
		if(isChild(age)) {
			return fare * 0.5;
		}

		if(isTeenager(age)) {
			return fare * 0.8;
		}

		return fare;
	}

	private boolean isChild(int age) {
		return age >= 6 && age < 13;
	}

	private boolean isTeenager(int age) {
		return age >= 13 && age < 19;
	}
}
