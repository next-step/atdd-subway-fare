package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.fare.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.LongDistanceFarePolicy;
import nextstep.subway.path.domain.fare.MiddleDistanceFarePolicy;
import nextstep.subway.path.domain.fare.ShortDistanceFarePolicy;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {
    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FEE = 900;

    private Path path;
    private DistanceFarePolicies distanceFarePolicies;

    @BeforeEach
    void setUp() {
        Section 교대_강남_구간 = new Section(new Station(1L, "교대역"), new Station(2L, "강남역"), 1, 1);
        Section 강남_양재_구간 = new Section(new Station(2L, "강남역"), new Station(3L, "양재역"), 1, 1);

        Line 이호선 = new Line("2호선", "green", 0, 교대_강남_구간);
        Line 신분당선 = new Line("신분당선", "red", ADDITIONAL_FEE, 강남_양재_구간);

        path = new Path(Set.of(이호선, 신분당선), new Sections(List.of(교대_강남_구간, 강남_양재_구간)));

        distanceFarePolicies = new DistanceFarePolicies(
                List.of(
                        new ShortDistanceFarePolicy(),
                        new MiddleDistanceFarePolicy(),
                        new LongDistanceFarePolicy()
                )
        );
    }

    @DisplayName("거리가 10km 미만이고, 추가 요금이 900원인 노선이 있을 때 기본요금에 900원이 더해진 값이 반환된다.")
    @Test
    void additionalFee() {
        // when
        int fare = path.calculateFare(distanceFarePolicies);

        // then
        assertThat(fare).isEqualTo(BASIC_FARE + ADDITIONAL_FEE);
    }
}