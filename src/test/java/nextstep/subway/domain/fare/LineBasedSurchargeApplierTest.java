package nextstep.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineBasedSurchargeApplierTest {

  @DisplayName("경로에 포함된 구간 중 추가요금이 제일 높은 구간의 추가요금만 적용")
  @Test
  void calculate() {
    // given
    var 구간_목록 = List.of(
        구간_생성(100),
        구간_생성(200),
        구간_생성(300)
    );
    var fare = Fare.baseFare();
    var applier = new LineBasedSurchargeApplier(구간_목록);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getSurcharge()).isEqualTo(300);
    assertThat(fare.getTotalFare()).isEqualTo(1550);
  }

  private Line 구간_생성(int extraFare) {
    return FixtureUtil.getBuilder(Line.class)
        .set("extraFare", extraFare)
        .set("sections", null)
        .sample();
  }
}