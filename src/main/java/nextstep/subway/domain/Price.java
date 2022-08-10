package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int BASE_PRICE = 0;

    @Column
    private int price;

    protected Price() { }

    private Price(int price) {
        validateNegativePrice(price);
        this.price = price;
    }

    public static Price from(int price) {
        return new Price(price);
    }

    private void validateNegativePrice(int price) {
        if (price < BASE_PRICE) {
            throw new IllegalArgumentException("노선의 추가 요금은 0 미만의 금액이 들어올 수 없습니다. 입력된 금액 : " + price);
        }
    }

    public int price() {
        return price;
    }
}
