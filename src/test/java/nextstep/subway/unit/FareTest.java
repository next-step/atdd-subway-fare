package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("운임요금 계산 테스트")
class FareTest {

    private static final int BASE_FARE = 1250;
    private static final int ELEVEN_KILO_FARE = 1350;
    private static final int FIFTY_ONE_KILO_FARE = 2250;

    @Test
    void 기본_요금_계산() {
        int distance = 5;

        int fare = Fare.BASIC.getFare(distance);

        assertThat(fare).isEqualTo(BASE_FARE);
    }

    @Test
    void 십킬로_초과_요금_계산() {
        int distance = 11;

        int fare = Fare.TEN_TO_FIFTY.getFare(distance);

        assertThat(fare).isEqualTo(ELEVEN_KILO_FARE);
    }

    @Test
    void 오십킬로_초과_요금_계산() {
        int distance = 51;

        int fare = Fare.OVER_TO_FIFTY.getFare(distance);

        assertThat(fare).isEqualTo(FIFTY_ONE_KILO_FARE);
    }

}
