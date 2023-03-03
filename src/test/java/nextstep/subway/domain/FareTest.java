package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    @Test
    void 기본_요금_계산() {
        Fare fare = Fare.of(10);
        int amount = fare.calculate(10);

        assertThat(fare).isEqualTo(Fare.DEFAULT);
        assertThat(amount).isEqualTo(1250);
    }

    @Test
    void 일차_추가_구간요금_계산() {
        Fare fare = Fare.of(15);
        int amount = fare.calculate(15);

        assertThat(fare).isEqualTo(Fare.SECTION1);
        assertThat(amount).isEqualTo(1350);
    }

    @Test
    void 이차_추가_구간요금_계산() {
        Fare fare = Fare.of(68);
        int amount = fare.calculate(68);

        assertThat(fare).isEqualTo(Fare.SECTION2);
        assertThat(amount).isEqualTo(1250 + 1000 + 100);
    }
}