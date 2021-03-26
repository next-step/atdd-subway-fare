package nextstep.subway.line.domain;

import nextstep.subway.member.domain.LoginMember;

import java.util.Optional;

public enum LineFare {
    ADULT(1250, 0),
    YOUTH(900, 20),
    CHILD(900, 50);

    private final int fare;
    private final int discountRate;

    LineFare(int fare, int discountRate) {
        this.fare = fare;
        this.discountRate = discountRate;
    }

    public int getFare() {
        return fare;
    }

    public int getDiscountRateOf(int fare) {
        double rate = discountRate / 100.0;
        return (int)(fare * rate);
    }

    public static LineFare ofMember(LoginMember loginMember) {
        return Optional.ofNullable(loginMember)
                    .map(LoginMember::getAge)
                    .map(age ->
                        age < 13 ? CHILD :
                        age < 19 ? YOUTH : ADULT)
                    .orElse(ADULT);
    }
}
