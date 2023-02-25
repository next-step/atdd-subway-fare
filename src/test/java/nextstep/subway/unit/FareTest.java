package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.FarePolicy.BASE_FARE;
import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    void 지하철_운임_요금을_생성한다(){
        Fare fare = new Fare(distance -> 0);
        assertThat(fare).isNotNull();
    }

    @Test
    void 지하철_운임_요금을_계산한다(){
        Fare fare = new Fare(distance -> 0);

        int amount = fare.calculate(10);

        assertThat(amount).isEqualTo(0);
    }

    @Test
    void 기본_요금_정책_중_거리_10km_이내는_기본_운임_요금이_발생한다(){
        Fare fare = new Fare(distance -> BASE_FARE);

        int amount = fare.calculate(10);

        assertThat(amount).isEqualTo(BASE_FARE);
    }

    @Test
    void 기본_요금_정책_중_거리_10km_초과_50km_이내는_5km_당_100원_추가_운임_요금이_발생한다(){
        Fare fare = new Fare(distance -> BASE_FARE);

        int amount = fare.calculate(50);

        assertThat(amount).isEqualTo(2050);
    }

    @Test
    void 기본_요금_정책_중_거리_50km_초과는_8km_당_100원_추가_운임_요금이_발생한다(){
        Fare fare = new Fare(distance -> BASE_FARE);

        int amount = fare.calculate(58);

        assertThat(amount).isEqualTo(2150);
    }
}