package nextstep.subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFare {
    private static final int MIN = 0;

    @Column(name = "additional_fare")
    private int value;

    protected AdditionalFare() {
    }

    public AdditionalFare(final int value) {
        if (value < MIN) {
            throw new IllegalArgumentException("추가 요금은 0 미만일 수 없습니다.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdditionalFare that = (AdditionalFare) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
