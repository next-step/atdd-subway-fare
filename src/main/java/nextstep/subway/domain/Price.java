package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private int value;

    public Price() {

    }

    public Price(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
