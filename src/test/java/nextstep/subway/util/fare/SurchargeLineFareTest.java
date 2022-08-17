package nextstep.subway.util.fare;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.util.fixture.MemberFixture.회원;
import static nextstep.subway.util.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SurchargeLineFareTest {
    private final SurchargeLineFare surchargeLineFare = new SurchargeLineFare();

    private final Line 일호선 = new Line("1호선", "green", 1000);
    private final Line 이호선 = new Line("2호선", "orange", 1200);
    private final Line 삼호선 = new Line("3호선", "blue", 300);

    private final Sections sections = new Sections(List.of(
            new Section(일호선, 교대역, 잠실역, 5, 10),
            new Section(이호선, 잠실역, 선릉역, 4, 6),
            new Section(삼호선, 강남역, 논현역, 3, 7)
    ));
    private Path path = new Path(sections);

    @DisplayName("추가 요금이 있는 노션을 이용할 경우 가장 큰 추가요금을 계산한다.")
    @Test
    void calculate() {
        assertThat(surchargeLineFare.calculate(path, 회원)).isEqualTo(1200);
    }
}
