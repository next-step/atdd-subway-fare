package nextstep.subway.maps.boarding.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("요금 계산 정책의 일급 컬렉션을 테스트한다.")
class FareCalculationPolicyGroupTest {

    @Mock
    private Boarding boarding;

    private FareCalculationPolicyGroup policyGroup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        policyGroup = new FareCalculationPolicyGroup(new DistanceProportionFareCalculationPolicy());
    }

    @DisplayName("정책이 올바르게 통과하는지 테스트한다.")
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
        final int fare = policyGroup.calculateFare(boarding);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}