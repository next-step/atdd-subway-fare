package nextstep.subway.util.fare;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.util.fixture.LineFixture.*;
import static nextstep.subway.util.fixture.MemberFixture.회원;
import static nextstep.subway.util.fixture.SectionFixture.십킬로미터_이하_구간목록;
import static nextstep.subway.util.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class OverFiftyKiloMeterFareTest {
    private final OverFiftyKiloMeterFare overFiftyKiloMeterFare = new OverFiftyKiloMeterFare();

    private Sections 오십킬로미터_초과_구간목록 = new Sections(List.of(
            new Section(일호선, 교대역, 잠실역, 10, 10),
            new Section(이호선, 잠실역, 선릉역, 20, 6),
            new Section(삼호선, 강남역, 논현역, 30, 7)
    ));

    private Path 오십킬로미터_초과_경로 = new Path(오십킬로미터_초과_구간목록);
    private Path 오십킬로미터_이하_경로 = new Path(십킬로미터_이하_구간목록);

    @DisplayName("50km 초과 경로는 8km당 100원의 추가 요금이 부과된다.")
    @Test
    void calculateOver50km() {
        assertThat(overFiftyKiloMeterFare.calculate(오십킬로미터_초과_경로, 회원)).isEqualTo(200);
    }

    @DisplayName("50km 이하의 경로는 추가 요금이 부과되지 않는다.")
    @Test
    void calculateNormal() {
        assertThat(overFiftyKiloMeterFare.calculate(오십킬로미터_이하_경로, 회원)).isZero();
    }
}
