package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.PricePolicy;
import nextstep.subway.price.PricePolicyCalculate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PricePolicyCalculateTest {

    PricePolicy 요금_정책;

    int 일곱살 = 7;
    int 열다섯살 = 15;
    int 열아홉살 = 19;

    int 기본요금 = 1250;

    private static final int BASE_DISTANCE = 10;
    private static final int DIVISION_ONE_BASE_DISTANCE = 5;

    private static final int DIVISION_ONE_DISTANCE = 50;
    private static final int OVER_DIVISION_ONE_BASE_DISTANCE = 8;

    @DisplayName("10km 이하 아이 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void childDiscountCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 일곱살, 0);

        assertThat(요금_정책.calculatePrice()).isEqualTo(450);
    }

    @DisplayName("10km초과 50km미만 아이 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {15, 20, 25, 30, 35, 40, 45, 50})
    public void childDiscountOfTenCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 일곱살, 0);

        int price = 십키로_이상_거리_요금_계산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo( childOfDiscount(price) );
    }

    @DisplayName("50km 초과 아이 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {50, 55, 60, 65, 70, 75, 80})
    public void childDiscountOfFiftyCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 일곱살, 0);

        int price = 오심키로_초과_거리_요금_게산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo( childOfDiscount(price) );
    }

    @DisplayName("10km 이하 청소년 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void teenagerDiscountCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열다섯살, 0);

        assertThat(요금_정책.calculatePrice()).isEqualTo(720);
    }

    @DisplayName("10km초과 50km미만 청소년 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {15, 20, 25, 30, 35, 40, 45, 50})
    public void teenagerDiscountOfTenCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열다섯살, 0);

        int price = 십키로_이상_거리_요금_계산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo(teenagerOfdiscount(price));
    }

    @DisplayName("50km 초과 청소년 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {50, 55, 60, 65, 70, 75, 80})
    public void teenagerDiscountOfFitfyCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열다섯살, 0);

        int price = 오심키로_초과_거리_요금_게산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo(teenagerOfdiscount(price));
    }

    @DisplayName("10km 이하 어른 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void adultDiscountCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열아홉살, 0);

        assertThat(요금_정책.calculatePrice()).isEqualTo(1250);
    }

    @DisplayName("10km초과 50km미만 어른 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {15, 20, 25, 30, 35, 40, 45, 50})
    public void adultDiscountOfTenCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열아홉살, 0);

        int price = 십키로_이상_거리_요금_계산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo(price);
    }

    @DisplayName("50km 초과 어른 할인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {50, 55, 60, 65, 70, 75, 80})
    public void adultDiscountOfFitfyCalculatePrice(int distance) {
        요금_정책 = new PricePolicyCalculate(distance, 열아홉살, 0);

        int price = 오심키로_초과_거리_요금_게산(distance);

        assertThat(요금_정책.calculatePrice()).isEqualTo(price);
    }

    private int 십키로_이상_거리_요금_계산(int distance) {
        return 기본요금 + calculateOverFare(distance - BASE_DISTANCE, DIVISION_ONE_BASE_DISTANCE);
    }

    private int 오심키로_초과_거리_요금_게산(int distance) {
        return 기본요금 +
            calculateOverFare(DIVISION_ONE_DISTANCE - BASE_DISTANCE, DIVISION_ONE_BASE_DISTANCE )
            + calculateOverFare(distance - DIVISION_ONE_DISTANCE,
            OVER_DIVISION_ONE_BASE_DISTANCE);
    }

    private int calculateOverFare(int distance, int baseOfDistance) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / baseOfDistance) + 1) * 100);
    }

    private Integer childOfDiscount(Integer price) {
        return (int) ((price - 350) * 0.5);
    }

    private Integer teenagerOfdiscount(Integer price) {
        return (int) ((price - 350) * 0.8);
    }

}
