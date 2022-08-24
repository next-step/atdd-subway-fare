package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FarePolicyTest {

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
