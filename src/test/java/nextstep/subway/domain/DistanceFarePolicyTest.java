package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    private static final int BASE_FARE = 1250;

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
    @Test
    void under10Km() {
        //given
        FarePolicy policy = new DistanceFarePolicy(10);

        //when
        int fare = policy.proceed(BASE_FARE);

        //then
        assertThat(fare).isEqualTo(BASE_FARE);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void over10KmUnder50Km(int multiply) {
        //given
        int distance = 10 + (5 * multiply);
        FarePolicy policy = new DistanceFarePolicy(distance);

        //when
        int fare = policy.proceed(BASE_FARE);

        //then
        int extraFare = 100 * multiply;
        assertThat(fare).isEqualTo(BASE_FARE + extraFare);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (50km 초과)")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void over50Km(int multiply) {
        //given
        int distance = 50 + (8 * multiply);
        FarePolicy policy = new DistanceFarePolicy(distance);

        //when
        int fare = policy.proceed(BASE_FARE);

        //then
        int extraFare = 800 + (100 * multiply);
        assertThat(fare).isEqualTo(BASE_FARE + extraFare);
    }


}