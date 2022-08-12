package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.fee.DistanceFeePolicy;

class DistanceFeePolicyTest {

	@Test
	@DisplayName("거리가 0보다 작을경우")
	void calculateFeeUnderZeroDistance() {
		assertThatThrownBy(() -> new DistanceFeePolicy(0))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new DistanceFeePolicy(-1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("기본운임 구간 요금 계산")
	void getBasicFEE() {
		DistanceFeePolicy distanceFeePolicy1 = new DistanceFeePolicy(1);
		DistanceFeePolicy distanceFeePolicy2 = new DistanceFeePolicy(9);

		assertThat(distanceFeePolicy1.calculateFee()).isEqualTo(1250);
		assertThat(distanceFeePolicy2.calculateFee()).isEqualTo(1250);
	}

	@Test
	@DisplayName("10km 초과 50km 이하 구간인경우")
	void getDivisionOneFEE() {
		DistanceFeePolicy distanceFeePolicy1 = new DistanceFeePolicy(10);
		DistanceFeePolicy distanceFeePolicy2 = new DistanceFeePolicy(11);

		assertThat(distanceFeePolicy1.calculateFee()).isEqualTo(1250);
		assertThat(distanceFeePolicy2.calculateFee()).isEqualTo(1350);
	}

	@Test
	@DisplayName("50km 초과 구간인경우")
	void getOverDivisionOneFEE() {
		DistanceFeePolicy distanceFeePolicy1 = new DistanceFeePolicy(50);
		DistanceFeePolicy distanceFeePolicy2 = new DistanceFeePolicy(51);
		DistanceFeePolicy distanceFeePolicy3 = new DistanceFeePolicy(58);
		DistanceFeePolicy distanceFeePolicy4 = new DistanceFeePolicy(59);

		assertThat(distanceFeePolicy1.calculateFee()).isEqualTo(2050);
		assertThat(distanceFeePolicy2.calculateFee()).isEqualTo(2150);
		assertThat(distanceFeePolicy3.calculateFee()).isEqualTo(2150);
		assertThat(distanceFeePolicy4.calculateFee()).isEqualTo(2250);
	}

}
