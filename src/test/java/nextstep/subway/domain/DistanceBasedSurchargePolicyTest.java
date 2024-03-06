package nextstep.subway.domain;

import static nextstep.subway.domain.DistanceBasedSurchargePolicy.FIFTY_KILOMETER_SURCHARGE_POLICY;
import static nextstep.subway.domain.DistanceBasedSurchargePolicy.TEN_KILOMETER_SURCHARGE_POLICY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceBasedSurchargePolicyTest {

  @DisplayName("추가요금 적용 대상이 아닌 경우 에러 발생")
  @Test
  void calculate_fail_추가요금_적용_대상_아님() {
    // given
    var distance = 10;

    // when
    var tenKilometerSurchargeResult = catchThrowable(() -> TEN_KILOMETER_SURCHARGE_POLICY.calculate(distance));
    var fiftyKilometerSurchargeResult = catchThrowable(() -> FIFTY_KILOMETER_SURCHARGE_POLICY.calculate(distance));

    // then
    assertThat(tenKilometerSurchargeResult).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("추가요금 적용 대상이 아닙니다.");
    assertThat(fiftyKilometerSurchargeResult).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("추가요금 적용 대상이 아닙니다.");
  }

  @DisplayName("5km 마다 100원의 추가요금을 부과한다.")
  @Test
  void calculate_5km_마다_100원의_추가요금() {
    // given
    var distance = 20;

    // when
    var surcharge = TEN_KILOMETER_SURCHARGE_POLICY.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(200);
  }

  @DisplayName("50km를 초과하는 이동거리에 대해서는 50km 까지만 추가요금을 부과한다.")
  @Test
  void calculate_50km_초과_이동거리에_대한_추가요금_부과() {
    // given
    var distance = 51;

    // when
    var surcharge = TEN_KILOMETER_SURCHARGE_POLICY.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(800);
  }

  @DisplayName("단위 거리당 추가요금을 계산한 뒤 나머지 거리는 올림으로 계산한다.")
  @Test
  void calculate_나누어_떨어지지_않는_거리는_올림해서_계산() {
    // given
    var distance = 13;

    // when
    var surcharge = TEN_KILOMETER_SURCHARGE_POLICY.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(100);
  }

  @DisplayName("8km 마다 100원의 추가요금을 부과한다.")
  @Test
  void calculate_8km_마다_100원의_추가요금() {
    // given
    var distance = 58;

    // when
    var surcharge = FIFTY_KILOMETER_SURCHARGE_POLICY.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(100);
  }

  @DisplayName("10km 에 대한 추가 요금 정책 목록")
  @Test
  void getApplicablePoliciesUnder10km() {
    // given
    int distance = 10;

    // when
    var policies = DistanceBasedSurchargePolicy.getApplicablePolicies(distance);

    // then
    assertThat(policies).isEmpty();
  }

  @DisplayName("50km 에 대한 추가 요금 정책 목록")
  @Test
  void getApplicablePoliciesAboutOver10km() {
    // given
    int distance = 50;

    // when
    var policies = DistanceBasedSurchargePolicy.getApplicablePolicies(distance);

    // then
    assertThat(policies).contains(TEN_KILOMETER_SURCHARGE_POLICY);
  }

  @DisplayName("60km 에 대한 추가 요금 정책 목록")
  @Test
  void getApplicablePoliciesAboutOver50km() {
    // given
    int distance = 60;

    // when
    var policies = DistanceBasedSurchargePolicy.getApplicablePolicies(distance);

    // then
    assertThat(policies).contains(
        TEN_KILOMETER_SURCHARGE_POLICY,
        FIFTY_KILOMETER_SURCHARGE_POLICY
    );
  }
}