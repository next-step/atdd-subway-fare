package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 관리")
class FareTest {

    @DisplayName("요금을 더할 수 있다")
    @Test
    void add() {
        // given
        Fare 천원 = Fare.of(BigDecimal.valueOf(1_000));
        Fare 이천원 = Fare.of(BigDecimal.valueOf(2_000));
        Fare 삼천원 = Fare.of(BigDecimal.valueOf(3_000));

        // when
        Fare fare = 천원.add(이천원);

        // then
        assertThat(fare).isEqualTo(삼천원);
    }

}