package nextstep.subway.payment;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Path;

public class PaymentRequestImpl implements PaymentRequest {

    private final Path path;
    private final Fare fare;
    private final int userAge;

    private PaymentRequestImpl(Path path, int userAge) {
        this(path, userAge, Fare.from(0));
    }

    private PaymentRequestImpl(Path path, int userAge, Fare fare) {
        this.path = path;
        this.userAge = userAge;
        this.fare = fare;
    }

    public static PaymentRequestImpl of(Path path, int userAge) {
        return new PaymentRequestImpl(path, userAge);
    }

    public static PaymentRequestImpl of(Path path, int userAge, Fare fare) {
        return new PaymentRequestImpl(path, userAge, fare);
    }

    @Override
    public void addFare(int addedFare) {
        fare.increase(addedFare);
    }

    @Override
    public Fare getFare() {
        return fare;
    }

    @Override
    public Path getPathResult() {
        return path;
    }

    @Override
    public int getLoginMemberAge() {
        return userAge;
    }
}
