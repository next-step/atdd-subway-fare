package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FareTest {

    @DisplayName("모든 요금 정책이 적용된 합계 요금")
    @Test
    void totalFare() {
        //given
        Fare fare = Fare.distance(10)
                .extraCharge(1000)
                .discount(15);
        //when
        int 거리_비례_요금 = fare.getDistanceFare();
        int 추가_요금 = fare.getExtraCharge();
        int 할인_금액 = fare.getDiscountFare();
        int 합계_요금 = fare.totalFare();

        //then
        assertAll(
                () -> assertThat(거리_비례_요금).isEqualTo(1250),
                () -> assertThat(추가_요금).isEqualTo(1000),
                () -> assertThat(할인_금액).isEqualTo(180),
                () -> assertThat(합계_요금).isEqualTo(2070)
        );

    }
}