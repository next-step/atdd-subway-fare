package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 관련 테스트")
class FareTest {

    @DisplayName("총 거리가 10km 미만일 경우, 1250의 요금이 발생한다")
    @Test
    void getFareAt10() {
        // when
        Fare fare = new Fare(10);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @DisplayName("총 거리가 11km인 경우, 1350의 요금이 발생한다")
    @Test
    void getFareAt11() {
        // when
        Fare fare = new Fare(11);

        // then
        assertThat(fare.getFare()).isEqualTo(1350);
    }

    @DisplayName("총 거리가 15km인 경우, 1350의 요금이 발생한다")
    @Test
    void getFareAt15() {
        // when
        Fare fare = new Fare(15);

        // then
        assertThat(fare.getFare()).isEqualTo(1350);
    }

    @DisplayName("총 거리가 16km인 경우, 1450의 요금이 발생한다")
    @Test
    void getFareAt16() {
        // when
        Fare fare = new Fare(16);

        // then
        assertThat(fare.getFare()).isEqualTo(1450);
    }

    @DisplayName("총 거리가 50km인 경우, 1350의 요금이 발생한다")
    @Test
    void getFareAt50() {
        // when
        Fare fare = new Fare(50);

        // then
        assertThat(fare.getFare()).isEqualTo(2050);
    }

    @DisplayName("총 거리가 57km인 경우, 2150의 요금이 발생한다")
    @Test
    void getFareAt57() {
        // when
        Fare fare = new Fare(57);

        // then
        assertThat(fare.getFare()).isEqualTo(2150);
    }

    @DisplayName("총 거리가 58km인 경우, 2150의 요금이 발생한다")
    @Test
    void getFareAt58() {
        // when
        Fare fare = new Fare(58);

        // then
        assertThat(fare.getFare()).isEqualTo(2150);
    }
}