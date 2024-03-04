package nextstep.path.application.fare.extra.distance;

import nextstep.path.application.fare.extra.distance.FareChain;
import nextstep.path.application.fare.extra.distance.FirstExtraFareHandler;
import nextstep.path.application.fare.extra.distance.PathFareHandler;
import nextstep.path.application.fare.extra.distance.SecondExtraFareHandler;
import nextstep.path.exception.FareApplyingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareChainTest {

    @ParameterizedTest
    @CsvSource(value =
            {
                    "10,1250",
                    "11,1350",
                    "16,1450",
                    "21,1550",
                    "46,2050",
                    "51,2150",
                    "59,2250",
                    "67,2350"
            }, delimiterString = ",")
    @DisplayName("요금 정책을 추가해 거리에 따라 요금을 반환받을 수 있다")
    void fareChainTest(final int distance, final long expected) {
        final FareChain fareChain = new FareChain()
                .addNext(new FirstExtraFareHandler())
                .addNext(new SecondExtraFareHandler());

        final long calculated = fareChain.calculate(distance);

        assertThat(calculated).isEqualTo(expected);
    }

    @Test
    @DisplayName("두번째 범위 시작점이 첫번째 범위 시작점보다 작을 수 없다")
    void standardDistanceLtPrevTest() {
        assertThatThrownBy(() -> new FareChain()
                .addNext(new SecondExtraFareHandler())
                .addNext(new FirstExtraFareHandler()))
                .isInstanceOf(FareApplyingException.class)
                .hasMessageContaining("standardDistance must be grater than previous standardDistance");
    }

    @Test
    @DisplayName("구간 요금 반복 범위가 1 보다 작을 수 없다")
    void fareIntervalLtOneDistance() {
        assertThatThrownBy(() -> new FareChain()
                .addNext(new ZeroFareInterval()))
                .isInstanceOf(FareApplyingException.class)
                .hasMessageContaining("fareInterval must be grater than");
    }

    private static class ZeroFareInterval extends PathFareHandler {

        @Override
        protected long calculateFare(final int distance) {
            return 0;
        }

        @Override
        protected int getStandardDistance() {
            return 1;
        }

        @Override
        protected int getFareInterval() {
            return 0;
        }
    }

    @Test
    @DisplayName("범위 시작점이 1 보다 작을 수 없다")
    void standardDistanceLtOneDistance() {
        assertThatThrownBy(() -> new FareChain()
                .addNext(new ZeroStandardDistance()))
                .isInstanceOf(FareApplyingException.class)
                .hasMessageContaining("standardDistance must be grater than");
    }

    private static class ZeroStandardDistance extends PathFareHandler {

        @Override
        protected long calculateFare(final int distance) {
            return 0;
        }

        @Override
        protected int getStandardDistance() {
            return 0;
        }

        @Override
        protected int getFareInterval() {
            return 1;
        }
    }
}
