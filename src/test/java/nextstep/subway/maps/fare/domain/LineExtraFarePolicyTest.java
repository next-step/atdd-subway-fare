package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.fare.utils.FareTestUtils;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineExtraFarePolicyTest {

    @DisplayName("노션별 추가 운임 계산")
    @Test
    public void calculate() {
        // given
        LineExtraFarePolicy lineExtraFarePolicyTest = new LineExtraFarePolicy();
        SubwayPath subwayPath = FareTestUtils.sampleSubwayPath();
        FareContext fareContext = new FareContext(subwayPath);

        // when
        lineExtraFarePolicyTest.calculate(fareContext);

        // then
        assertThat(fareContext.getFare().getValue()).isEqualTo(FareContext.DEFAULT_FARE + 1000);
    }
}
