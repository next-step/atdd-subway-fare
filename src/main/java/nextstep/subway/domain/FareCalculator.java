package nextstep.subway.domain;

public class FareCalculator {

	public static int calculateAmount(Path path, Integer age) {
		int fare = Fare.calculateAmount(path) + path.getExtraFareOnLine();
		return Discount.calculateDiscountAmount(fare, age);
	}
}
