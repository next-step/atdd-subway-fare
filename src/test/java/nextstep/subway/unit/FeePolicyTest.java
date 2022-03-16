package nextstep.subway.unit;

import nextstep.subway.domain.fee.FeeCondition;
import nextstep.subway.domain.fee.FiftyKmOverCondition;
import nextstep.subway.domain.fee.FiftyKmUnderCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.fee.FeeCondition.EXTRA_FEE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 test")
public class FeePolicyTest {

    @DisplayName("50미만의 거리는 5Km당 100원 추가")
    @Test
    void 오십Km_이하는_100원추가() {
        FiftyKmUnderCondition 오십이하정책 = new FiftyKmUnderCondition();
        int 오십이하 = 12;
        assertThat(오십이하정책.isInclude(오십이하)).isTrue();
        assertThat(오십이하정책.calculateFee(오십이하)).isEqualTo(FeeCondition.DEFAULT_FEE + EXTRA_FEE);
    }

    @DisplayName("50초과의 거리는 8Km당 100원 추가")
    @Test
    void 오십Km_초과는_100원추가() {
        FiftyKmOverCondition 오십이상정책 = new FiftyKmOverCondition();

        assertThat(오십이상정책.isInclude(FeeCondition.MAX_REFERENCE_DISTANCE)).isTrue();
        assertThat(오십이상정책.calculateFee(FeeCondition.MAX_REFERENCE_DISTANCE)).isEqualTo(FeeCondition.DEFAULT_FEE + EXTRA_FEE);

    }

}
