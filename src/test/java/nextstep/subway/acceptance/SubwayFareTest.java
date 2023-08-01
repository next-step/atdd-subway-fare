package nextstep.subway.acceptance;

import nextstep.subway.domain.SubwayFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayFareTest {

    @DisplayName("이용 거리가 10km 이내인 경우 기본운임 요금을 반환한다")
    @Test
    void calculateBasicFare() {
        // given
        SubwayFare subwayFare = new SubwayFare();

        // when
        int distance = 10;
        int fare = subwayFare.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("이용 거리가 10km 초과 ~ 50km 이내인 경우 5km 마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over10_Upto50() {
        // given
        SubwayFare subwayFare = new SubwayFare();

        // when
        int fare = subwayFare.calculateFare(16);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("이용 거리가 50km 초과 시 8km마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over50() {
        // given
        SubwayFare subwayFare = new SubwayFare();

        // when
        int fare = subwayFare.calculateFare(58);

        // then
        assertThat(fare).isEqualTo(2350);
    }

    @DisplayName("요금계산 시 거리가 0보다 작은 경우 에러가 발생한다")
    @Test
    void calculateFare_InvalidDistance_Exception() {
        // given
        SubwayFare subwayFare = new SubwayFare();

        // when, then
        assertThatThrownBy(() -> subwayFare.calculateFare(-1))
                .isInstanceOf(RuntimeException.class)
                .message().isEqualTo("거리가 올바르지 않습니다.");
    }
}
