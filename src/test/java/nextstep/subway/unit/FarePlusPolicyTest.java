package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/*
    노선별 추가 요금
    추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
    경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

    로그인 사용자의 경우 연령별 요금으로 계산
    청소년: 운임에서 350원을 공제한 금액의 20%할인
    어린이: 운임에서 350원을 공제한 금액의 50%할인
*/

public class FareDiscountPolicyTest {

    @DisplayName("구간 거리 10km이하 요금 조회 ")
    @Test
    void findFare_10km이하() {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(8);
        assertThat(distanceFarePolicy.calculateFare()).isEqualTo(1250);
    }
    
    @DisplayName("구간 거리 10 ~ 50 요금 조회")
    @Test
    void findFare_10km이상_50km이하() {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(15);
        assertThat(distanceFarePolicy.calculateFare()).isEqualTo(1350);
    }

    @DisplayName("구간 거리 50km 이상 요금 조회")
    @Test
    void findFare_50km이상() {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(66);
        assertThat(distanceFarePolicy.calculateFare()).isEqualTo(2250);
    }

}
