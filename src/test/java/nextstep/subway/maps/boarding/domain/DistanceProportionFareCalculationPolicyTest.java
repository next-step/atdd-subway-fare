package nextstep.subway.maps.boarding.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("거리비례제 테스트")
class DistanceProportionFareCalculationPolicyTest {

    @Mock
    private Boarding boarding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("각 거리 별로 계산을 올바르게 수행하는지 테스트한다.")
    @ParameterizedTest(name = "Fare is {1} won for {0} kilometers.")
    @CsvSource({
            "7, 1250",
            "12, 1350",
            "57, 2150",
    })
    void distanceProportion(int distance, int expectedFare) {

        // stubbing
        when(boarding.getBoardingDistance()).thenReturn(distance);

        // when
        final FareCalculationContext context = new FareCalculationContext(boarding);
        final DistanceProportionFareCalculationPolicy policy = new DistanceProportionFareCalculationPolicy();
        final int fare = policy.calculateFare(context);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}