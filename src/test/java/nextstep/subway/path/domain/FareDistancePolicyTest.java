package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationSteps.역삼역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareDistancePolicyTest {

    private final static int DEFAULT_FARE = 1_250;

    @DisplayName("거리가 10 이하일때 기본 요금임을 확인")
    @Test
    void defaultFare() {
        //given
        FarePolicy fareDistance = new FareDistancePolicy(0);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인")
    @Test
    void over10KmFare() {
        //given
        FarePolicy fareDistance = new FareDistancePolicy(12);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE+100);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인2")
    @Test
    public void over10KmFare2() {
        //given
        FarePolicy fareDistance = new FareDistancePolicy(16);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE + 200);
    }

    @DisplayName("거리가 50 초과 일때 요금 확인")
    @Test
    public void over50KmFare() {
        //given
        FarePolicy fareDistance = new FareDistancePolicy(51);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE + 600);
    }
}
