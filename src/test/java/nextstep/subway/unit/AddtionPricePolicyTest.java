package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.PricePolicy;
import nextstep.subway.price.addtionPrice.AddtionPricePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AddtionPricePolicyTest {

    private PricePolicy pricePolicy;

    @DisplayName("추가요금 더하기")
    @ParameterizedTest
    @ValueSource(ints = {0, 100, 500, 1000, 1500, 2000})
    void addtionPrice(int price) {
        int addtionPrice = 100;

        pricePolicy = new AddtionPricePolicy(price, addtionPrice);

        assertThat(pricePolicy.calculatePrice()).isEqualTo(price + addtionPrice);
    }

}
