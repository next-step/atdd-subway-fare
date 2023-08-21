package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.discount.age.ChildrenDiscountPolicy;
import nextstep.subway.path.domain.discount.age.DefaultAgeDiscountPolicy;
import nextstep.subway.path.domain.discount.age.TeenagerDiscountPolicy;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.distance.LongDistanceFarePolicy;
import nextstep.subway.path.domain.fare.distance.MiddleDistanceFarePolicy;
import nextstep.subway.path.domain.fare.distance.ShortDistanceFarePolicy;
import nextstep.subway.path.domain.fare.line.BasicLineFarePolicy;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
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
    private static final int ADDITIONAL_FARE = 900;

    private Path path;
    private DistanceFarePolicies distanceFarePolicies;
    private LineFarePolicy lineFarePolicy;

    @BeforeEach
    void setUp() {
        Section 교대_강남_구간 = new Section(new Station(1L, "교대역"), new Station(2L, "강남역"), 1, 1);
        Section 강남_양재_구간 = new Section(new Station(2L, "강남역"), new Station(3L, "양재역"), 1, 1);

        Line 이호선 = new Line("2호선", "green", 0, 교대_강남_구간);
        Line 신분당선 = new Line("신분당선", "red", ADDITIONAL_FARE, 강남_양재_구간);

        path = new Path(new Lines(Set.of(이호선, 신분당선)), new Sections(List.of(교대_강남_구간, 강남_양재_구간)));

        distanceFarePolicies = new DistanceFarePolicies(
                List.of(
                        new ShortDistanceFarePolicy(),
                        new MiddleDistanceFarePolicy(),
                        new LongDistanceFarePolicy()
                )
        );

        lineFarePolicy = new BasicLineFarePolicy();
    }

    @DisplayName("거리가 10km 미만이고, 추가 요금이 900원인 노선이 있을 때 기본요금에 900원이 더해진 값이 반환된다.")
    @Test
    void additionalFare() {
        // when
        int fare = path.calculateFare(distanceFarePolicies, lineFarePolicy, new DefaultAgeDiscountPolicy());

        // then
        assertThat(fare).isEqualTo(BASIC_FARE + ADDITIONAL_FARE);
    }

    @DisplayName("요금 2150원에 청소년 할인 정책이 적용된 금액이 반환된다.")
    @Test
    void discountFareTeenager() {
        // when
        int fare = path.calculateFare(distanceFarePolicies, lineFarePolicy, new TeenagerDiscountPolicy());

        // then
        int totalFare = BASIC_FARE + ADDITIONAL_FARE;
        assertThat(fare).isEqualTo((int) (350 + (totalFare - 350) * 0.8));
    }

    @DisplayName("요금 2150원에 어린이 할인 정책이 적용된 금액이 반환된다.")
    @Test
    void discountFareChildren() {
        // when
        int fare = path.calculateFare(distanceFarePolicies, lineFarePolicy, new ChildrenDiscountPolicy());

        // then
        int totalFare = BASIC_FARE + ADDITIONAL_FARE;
        assertThat(fare).isEqualTo((int) (350 + (totalFare - 350) * 0.5));
    }
}