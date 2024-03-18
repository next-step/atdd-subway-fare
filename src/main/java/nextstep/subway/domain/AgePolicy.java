package nextstep.subway.domain;

public class AgePolicy implements FarePolicy {
	private final int age;

	public AgePolicy(int age) {
		this.age = age;
	}

	@Override
	public int getAdditionalFee() {
		return 0;
	}

	@Override
	public int getDiscountFee() {
		if(isChild(age) || isTeenager(age)) {
			return 350;
		}

		return 0;
	}

	@Override
	public double getDiscountPercent() {
		if(isChild(age)) {
			return 0.5;
		}

		if(isTeenager(age)) {
			return 0.2;
		}

		return 0;
	}

	private boolean isChild(int age) {
		return age >= 6 && age < 13;
	}

	private boolean isTeenager(int age) {
		return age >= 13 && age < 19;
	}
}
