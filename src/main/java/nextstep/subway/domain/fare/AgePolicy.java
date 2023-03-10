package nextstep.subway.domain.fare;

public class AgePolicy implements FarePolicy {
    private static final AgePolicy INSTANCE = new AgePolicy();

    private AgePolicy() {
    }

    public static AgePolicy getInstance() {
        return INSTANCE;
    }

    @Override
    public Fare addFare(Fare fare, FareBasis fareBasis) {
        int age = fareBasis.getAge();
        AgeFareType ageFareType = AgeFareType.findByAge(age);
        int discountFare= ageFareType.discountFare(fare.extraTotalFare());
        return fare.addDisCountFare(discountFare);
    }
}
