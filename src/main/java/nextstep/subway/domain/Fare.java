package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    @Column(name = "fare")
    private int value;

    public Fare() {

    }

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
