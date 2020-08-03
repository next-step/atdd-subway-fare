package nextstep.subway.maps.boarding.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("미성년자 요금 계산에 대한 정책 테스트")
class MinorsFareCalculationPolicyTest {

    @Mock
    private Boarding boarding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("연령별로 요금을 올바르게 계산하는지 테스트한다.")
    @ParameterizedTest
    @CsvSource({
            "7, 450",
            "14, 720",
            "20, 1250",
    })
    void distanceProportion(int age, int expectedFare) {

        // stubbing
        final Age mockAge = getAge(age);
        when(boarding.getBoardingDistance()).thenReturn(5);
        when(boarding.getPassengerAge()).thenReturn(mockAge);
        final FareCalculationContext context = new FareCalculationContext(boarding);
        context.setCalculationResult(1250);

        // when
        final MinorsFareCalculationPolicy policy = new MinorsFareCalculationPolicy();
        final int fare = policy.calculateFare(context);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private Age getAge(int age) {
        if (age > 19) {
            return Age.ADULT;
        }

        if (age > 13) {
            return Age.YOUTH;
        }

        return Age.CHILD;
    }

}