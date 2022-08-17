package nextstep.subway.util.fixture;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

import java.util.List;

import static nextstep.subway.util.fixture.LineFixture.*;
import static nextstep.subway.util.fixture.StationFixture.*;

public class SectionFixture {
    public static final Sections 십킬로미터_이하_구간목록 = new Sections(List.of(
            new Section(일호선, 교대역, 잠실역, 3, 10),
            new Section(이호선, 잠실역, 선릉역, 3, 6),
            new Section(삼호선, 강남역, 논현역, 3, 7)
    ));
}
