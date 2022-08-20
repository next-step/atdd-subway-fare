package nextstep.subway.unit;

import nextstep.subway.domain.AgeDiscountCalculator;
import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.SubwayFarePolicyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeDiscountCalculatorTest {
    private final int baseFare = 1250;
    private int totalFare;
    private SubwayFarePolicyType 어린이;
    private SubwayFarePolicyType 청소년;
    private SubwayFarePolicyType 어른;
    private FareCalculator ageDiscountCalculator;

    @BeforeEach
    void setUp() {
        어린이 = SubwayFarePolicyType.CHILD;
        청소년 = SubwayFarePolicyType.YOUTH;
        어른 = SubwayFarePolicyType.ADULT;

        totalFare = 0;
    }

    @DisplayName("어린이 요금 할인")
    @Test
    public void discountForChild(){
        ageDiscountCalculator  = new AgeDiscountCalculator(어린이);
        totalFare = ageDiscountCalculator.calculate(baseFare, 100);

        assertThat(totalFare).isEqualTo(450);
    }
    @DisplayName("청소년 요금 할인")
    @Test
    public void discountForYouth(){
        ageDiscountCalculator  = new AgeDiscountCalculator(청소년);
        totalFare = ageDiscountCalculator.calculate(baseFare, 100);

        assertThat(totalFare).isEqualTo(720);
    }
    @DisplayName("어른 요금 할인")
    @Test
    public void noramlFare(){
        ageDiscountCalculator  = new AgeDiscountCalculator(어른);
        totalFare = ageDiscountCalculator.calculate(baseFare, 100);

        assertThat(totalFare).isEqualTo(1250);
    }

    @DisplayName("나이별 할인 요금")
    @ParameterizedTest
    @CsvSource({
            "1250, CHILD, 450",
            "2150, YOUTH, 1440",
            "2550, ADULT, 2550"
    })
    public void discountForAge(int totalFare, String person, int discountFare){
        ageDiscountCalculator = new AgeDiscountCalculator(SubwayFarePolicyType.valueOf(person));
        int result = ageDiscountCalculator.calculate(totalFare, 100);
        assertThat(result).isEqualTo(discountFare);
    }


}
