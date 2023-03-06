package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.fixtures.FareFixtures.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {


    @DisplayName("이용거리가 10km이내일경우 기본요금을 부과한다")
    @Test
    void 이용거리가_10km이내일경우_기본요금을_부과한다() {
        // when
        Fare fare = Fare.of(new DistanceFarePolicy(0), 10);

        // then
        assertThat(fare.get()).isEqualTo(DEFAULT_FARE);
    }

    /**
     * 10km초과∼50km까지(5km마다 100원)
     */
    @DisplayName("거리가 16km일경우 200원의 추가요금이 포함된다")
    @Test
    void 거리가_20km일경우_200원의_추가요금이_포함된다() {
        // when
        Fare fare = Fare.of(new DistanceFarePolicy(0), 16);

        // then
        assertThat(fare.get()).isEqualTo(DEFAULT_FARE + 200);
    }

    /**
     * 50km초과 시 (8km마다 100원)
     */
    @DisplayName("거리가 60km일경우 800원의 추가요금이 포함된다")
    @Test
    void 거리가_60km일경우_700원의_추가요금이_포함된다() {
        // when
        Fare fare = Fare.of(new DistanceFarePolicy(0), 60);

        // then
        assertThat(fare.get()).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("이용거리가 10km이내이고 노선추가요금이 있을경우 추가요금을 부과한다")
    @Test
    void 이용거리가_10km이내이고_노선추가요금이_있을경우_추가요금을_부과한다() {
        // given
        int extraFare = 600;

        // when
        Fare fare = Fare.of(new DistanceFarePolicy(extraFare), 10);

        // then
        assertThat(fare.get()).isEqualTo(DEFAULT_FARE + extraFare);
    }
}
