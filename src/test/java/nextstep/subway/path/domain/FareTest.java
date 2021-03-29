package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    @DisplayName("거리가 10km 이내일 경우 기본 요금")
    void getFare10KmUnder() {
        Fare fare = new Fare(10);
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 11km ~ 50km 이내일 경우 5Km 당 100원 추가요금")
    void getFare10KmOverAnd50KmUnder1() {
        Fare fare = new Fare(20);
        assertThat(fare.getFare()).isEqualTo(1450);
    }

    @Test
    @DisplayName("거리가 11km ~ 50km 이내일 경우 5Km 당 100원 추가요금")
    void getFare10KmOverAnd50KmUnder2() {
        Fare fare = new Fare(50);
        assertThat(fare.getFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("거리가 50km 초과될 경우 8Km 당 100원 추가요금")
    void getFare50KmOver1() {
        Fare fare = new Fare(51);
        assertThat(fare.getFare()).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리가 50km 초과될 경우 8Km 당 100원 추가요금")
    void getFare50KmOver2() {
        Fare fare = new Fare(66);
        assertThat(fare.getFare()).isEqualTo(2250);
    }
}
