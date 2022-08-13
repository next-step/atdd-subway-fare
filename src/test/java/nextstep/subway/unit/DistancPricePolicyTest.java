package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.DistancePricePolicy;
import nextstep.subway.price.PricePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistancPricePolicyTest {

    private int BASIC_PRICE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int DIVISION_ONE_BASE_DISTANCE = 5;

    private static final int DIVISION_ONE_DISTANCE = 50;
    private static final int OVER_DIVISION_ONE_BASE_DISTANCE = 8;

    @DisplayName("기본요금 나오는지를 확인합니다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void basicPriceTest(int distance) {
        PricePolicy 요금정책 = new DistancePricePolicy(distance);

        assertThat(요금정책.calculatePrice()).isEqualTo(BASIC_PRICE);
    }

    @DisplayName("10km 초과시 - 5km마다 100원 추가")
    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 25, 30, 35, 40, 45, 50})
    void overTenDistancePriceTest(int distance) {
        int price =
            BASIC_PRICE + calculateOverFare(distance - BASE_DISTANCE, DIVISION_ONE_BASE_DISTANCE);

        PricePolicy 요금정책 = new DistancePricePolicy(distance);

        assertThat(요금정책.calculatePrice()).isEqualTo(price);
    }

    @DisplayName("50km 초과시 - 8km마다 100원")
    @ParameterizedTest
    @ValueSource(ints = {50, 55, 60, 65, 70, 75, 80})
    void overFiftyDistancePriceTest(int distance) {
        int price =
            BASIC_PRICE + calculateOverFare(DIVISION_ONE_DISTANCE - BASE_DISTANCE, DIVISION_ONE_BASE_DISTANCE )
                + calculateOverFare(distance - DIVISION_ONE_DISTANCE,
                    OVER_DIVISION_ONE_BASE_DISTANCE);

        PricePolicy 요금정책 = new DistancePricePolicy(distance);

        assertThat(요금정책.calculatePrice()).isEqualTo(price);
    }

    private int calculateOverFare(int distance, int baseOfDistance) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / baseOfDistance) + 1) * 100);
    }

}
