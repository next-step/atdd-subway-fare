package nextstep.path.application;

import nextstep.path.exception.FareApplyingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareChainTest {
    /**
     * 0~5 1250
     * 5~10 2 마다 100
     * 10~20 10 마다 100
     * 20~   5 마다 100
     * 일 경우 30 거리 기준
     * 5 1250
     * 6 1350
     * 8 1450
     * 10 1550
     * 20 1650
     * 25 1750
     * 30 1850
     */
    @Test
    @DisplayName("요금 정책을 추가해 거리에 따라 요금을 반환받을 수 있다")
    void fareChainTest() {
        final FareChain fareChain = new FareChain()
                .nextRange(5, 2)
                .nextRange(10, 10)
                .nextRange(20, 5);

        final long calculated = fareChain.calculate(30);

        assertThat(calculated).isEqualTo(1850);
    }

    @Test
    @DisplayName("두번째 범위 시작점이 첫번째 범위 시작점보다 작을 수 없다")
    void standardDistanceLtPrevTest() {
       assertThatThrownBy(() -> new FareChain()
               .nextRange(5, 2)
               .nextRange(4, 1))
               .isInstanceOf(FareApplyingException.class)
               .hasMessageContaining("standardDistance must be grater than previous standardDistance");
    }
    @Test
    @DisplayName("구간 요금 반복 범위가 1 보다 작을 수 없다")
    void fareIntervalLtOneDistance() {
       assertThatThrownBy(() -> new FareChain()
               .nextRange(1, 0))
               .isInstanceOf(FareApplyingException.class)
               .hasMessageContaining("fareInterval must be grater than");
    }
    @Test
    @DisplayName("범위 시작점이 1 보다 작을 수 없다")
    void standardDistanceLtOneDistance() {
       assertThatThrownBy(() -> new FareChain()
               .nextRange(0, 1))
               .isInstanceOf(FareApplyingException.class)
               .hasMessageContaining("standardDistance must be grater than");
    }
}
