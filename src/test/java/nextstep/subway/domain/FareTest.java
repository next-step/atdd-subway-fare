package nextstep.subway.domain;

import nextstep.subway.domain.policy.PathByFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest extends FareManagerLoaderTest {

    @Test
    @DisplayName("요금 계산이 완료되지 전에 요금을 조회할 수 없다.")
    void invalidToInt_Not_Done() {
        Fare fare = Fare.chaining();

        assertThatThrownBy(() -> fare.toInt()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("계산이 완료된 후 다시 계산을 할 수 없다.")
    void invalidCalculate_With_Done() {
        PathByFare pathByFare = PathByFare.builder().distance(10).build();
        Fare fare = Fare.chaining().calculate(pathByFare);

        assertThatThrownBy(() -> fare.calculate(pathByFare)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("요금 계산을 하면 계산이 완료된다.")
    void calculate_done() {
        PathByFare pathByFare = PathByFare.builder().distance(10).build();
        Fare fare = Fare.chaining().calculate(pathByFare);

        assertThat(fare.toInt()).isEqualTo(1_250);
    }
}