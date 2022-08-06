package nextstep.subway.unit;

import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.Fare.calculate;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    void 거리가_10인_경우_요금을_계산한다() {
        // given
        int fare = calculate(10);

        // then
        assertThat(fare).isEqualTo(1_250);
    }

    @Test
    void 거리가_50인_경우_요금을_계산한다() {
        // given
        int fare = calculate(50);

        // then
        assertThat(fare).isEqualTo(2_250);
    }

    @Test
    void 거리가_51인_경우_요금을_계산한다() {
        // given
        int fare = calculate(51);

        // then
        assertThat(fare).isEqualTo(1_950);
    }
}
