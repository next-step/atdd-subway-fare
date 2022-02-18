package nextstep.subway.domain.fare;

import java.util.Objects;

public class Fare {

	private int fare;

	public Fare(int fare) {
		this.fare = fare;
	}

	public static Fare valueOf(int fare) {
		return new Fare(fare);
	}

	public Fare plus(Fare addend) {
		return new Fare(addend.fare + fare);
	}

	public int getAmount() {
		return fare;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Fare)) return false;
		Fare fare1 = (Fare) o;
		return fare == fare1.fare;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fare);
	}

	@Override
	public String toString() {
		return "Fare{" +
				"fare=" + fare +
				'}';
	}
}
