package nextstep.subway.maps.boarding.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("노선별 추가 요금 정책 테스트")
class ExtraFareCalculationPolicyTest {

    @Mock
    private Boarding boarding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("각 거리 별로 계산을 올바르게 수행하는지 테스트한다.")
    @ParameterizedTest(name = "Fare is {2} won for {0} kilometers with extra fare({1} won).")
    @CsvSource({
            "7, 900, 2150",
            "12, 500, 1850",
            "57, 1000, 3150",
    })
    void distanceProportion(int distance, int extraFare, int expectedFare) {

        // stubbing
        when(boarding.getBoardingDistance()).thenReturn(distance);
        when(boarding.getMaximumFare()).thenReturn(extraFare);
        final FareCalculationContext context = new FareCalculationContext(boarding);
        final DistanceProportionFareCalculationPolicy distancePolicy = new DistanceProportionFareCalculationPolicy();
        distancePolicy.calculateFare(context);

        // when
        final ExtraFareCalculationPolicy policy = new ExtraFareCalculationPolicy();
        final int fare = policy.calculateFare(context);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}