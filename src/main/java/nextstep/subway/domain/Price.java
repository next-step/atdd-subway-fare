package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.exception.CreateException;

@Embeddable
public class Price {

    @Column(name = "price")
    private int value;

    public Price() {
    }

    private Price(final int value) {
        this.value = value;
    }


    public static Price of(final int value) {
        if (value < 0) {
            throw new CreateException("추가 요금은 음수가 될 수 없습니다.");
        }
        return new Price(value);
    }

}
