package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.FarePolicyConfig;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondtion;
import nextstep.subway.domain.farepolicy.discountcondition.KidsFareDiscountCondtion;

@DisplayName("경로 요금 계산 결과 테스트")
@ExtendWith(MockitoExtension.class)
public class FareCalculatorTest {
    @Mock private Section section1;
    @Mock private Section section2;
    @Mock private Section section3;
    @Mock private Sections sections;

    private FareCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FarePolicyConfig().fareCalculator();
    }

    @CsvSource({
        "6,9,900",
        "6,10,900",
        "12,11,950",
        "13,50,2160",
        "18,51,2240",
        "19,51,3150",
        "19,59,3250"
    })
    @DisplayName("요금 계산 테스트")
    @ParameterizedTest
    void calculate(int age, int distance, int totalCost) {
        // Given
        when(section1.getAdditionalFare()).thenReturn(0);
        when(section2.getAdditionalFare()).thenReturn(500);
        when(section3.getAdditionalFare()).thenReturn(900);

        List<Section> dummySections = Arrays.asList(section1, section2, section3);
        when(sections.getSections()).thenReturn(dummySections);
        when(sections.totalDistance()).thenReturn(distance);

        Path path = new Path(sections);

        // When, Then
        FareDiscountCondtion fareDiscountPolicy = new KidsFareDiscountCondtion(age);
        assertThat(calculator.calculate(path, fareDiscountPolicy)).isEqualTo(totalCost);
    }
}
