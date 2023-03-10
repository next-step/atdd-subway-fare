package nextstep.subway.domain.fare;

public class Fare {
    private final int distanceFare;
    private final int extraLineFare;
    private final int discountFare;

    public Fare() {
        this.distanceFare = 0;
        this.extraLineFare = 0;
        this.discountFare = 0;
    }

    public Fare(int distanceFare, int extraLineFare, int discountFare) {
        this.distanceFare = distanceFare;
        this.extraLineFare = extraLineFare;
        this.discountFare = discountFare;
    }

    public Fare addDistanceFare(int distanceFare) {
        validateMinusFare(distanceFare);
        return new Fare(distanceFare, this.extraLineFare, this.discountFare);
    }

    public Fare addExtraLineFare(int extraLineFare) {
        validateMinusFare(extraLineFare);
        return new Fare(this.distanceFare, extraLineFare, this.discountFare);
    }

    public Fare addDisCountFare(int discountFare) {
        validateMinusFare(discountFare);
        return new Fare(this.distanceFare, this.extraLineFare, discountFare);
    }


    public int calculateTotalFare() {
        return distanceFare + extraLineFare - discountFare;
    }

    private void validateMinusFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("계산하고자 하는 요금 값은 음수일 수 없습니다.");
        }
    }

    public int extraTotalFare() {
        return this.distanceFare + this.extraLineFare;
    }
}
