package nextstep.subway.unit;


import nextstep.subway.domain.farecalculation.FareCalculation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("요금 계산 테스트")
public class FareCalculationTest {

    private final int BASE_FARE = 1250;
    public final int ADD_FARE_10KM = 100;
    public final int ADD_FARE_50KM = 100;

    @DisplayName("거리가 10km 미만이면 요금이 1250원이 계산된다.")
    @Test
    void 십키로미터미만_요금계산() {
        //given
        Long distance = 9L;

        //when
        FareCalculation fareCalculation = new FareCalculation();
        int fare = fareCalculation.getFareByDistance(distance);

        //then
        assertThat(fare).isEqualTo(BASE_FARE);
    }

    @DisplayName("거리가 10km 초과이고 50km 미만이면 5km 마다 100원의 추가요금이 붙는다.")
    @Test
    void 십키로미터초과_오십키로미터_요금계산() {
        //given
        Long distance = 12L;

        //when
        FareCalculation fareCalculation = new FareCalculation();
        int fare = fareCalculation.getFareByDistance(distance);

        //then
        assertThat(fare).isEqualTo(BASE_FARE + ADD_FARE_10KM);
    }

    @DisplayName("거리가 50km 초과이면 8km 마다 100원의 추가 요금이 붙는다.")
    @Test
    void 오십키로미터초과_요금계산() {
        //given
        Long distance = 51L;

        //when
        FareCalculation fareCalculation = new FareCalculation();
        int fare = fareCalculation.getFareByDistance(distance);

        //then
        assertThat(fare).isEqualTo(BASE_FARE + ADD_FARE_10KM * 8 + ADD_FARE_50KM * 1);
    }
}
