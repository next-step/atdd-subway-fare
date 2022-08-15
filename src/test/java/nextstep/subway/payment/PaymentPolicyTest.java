package nextstep.subway.payment;

import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.fixture.LoginMemberAge.ADULT_AGE;
import static nextstep.subway.fixture.LoginMemberAge.ANONYMOUS_AGE;
import static org.assertj.core.api.Assertions.*;

class PaymentPolicyTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Path 교대역_양재역;

    /**
     * 교대역    --- *2호선*(0원, 3km, 3분)   ---     강남역
     * |                                                |
     * *3호선*(500원, 10km, 5분)                     *신분당선* (1000원, 3km, 3분)
     * |                                                 |
     * 남부터미널역  --- *3호선*(500원, 10km, 5분) ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", Fare.from(1_000));
        이호선 = new Line("2호선", "red", Fare.from(0));
        삼호선 = new Line("3호선", "red", Fare.from(500));

        신분당선.addSection(createSectionBuilder(강남역, 양재역, Distance.from(3), Duration.from(3)));
        이호선.addSection(createSectionBuilder(교대역, 강남역, Distance.from(3), Duration.from(3)));
        삼호선.addSection(createSectionBuilder(교대역, 남부터미널역, Distance.from(10), Duration.from(5)));
        삼호선.addSection(createSectionBuilder(남부터미널역, 양재역, Distance.from(10), Duration.from(5)));

        SubwayMap subwayMap = new SubwayMap(Lists.newArrayList(신분당선, 이호선, 삼호선));
        교대역_양재역 = subwayMap.findPath(교대역, 양재역);
    }

    @Test
    @DisplayName("거리기반 요금정책")
    void distanceFarePolicy() {
        PaymentPolicy distanceFarePolicy = new DistancePaymentPolicy();

        PaymentRequestImpl paymentRequest = PaymentRequestImpl.of(교대역_양재역, ANONYMOUS_AGE);

        distanceFarePolicy.pay(paymentRequest);

        assertThat(paymentRequest.getFare().fare()).isEqualTo(1_250);
    }

    @Test
    @DisplayName("노선 중 최대 금액 정책")
    void mostExpensiveLineFarePolicy() {
        LinePaymentPolicy linePaymentPolicy = new LinePaymentPolicy();

        PaymentRequestImpl paymentRequest = PaymentRequestImpl.of(교대역_양재역, ANONYMOUS_AGE);

        linePaymentPolicy.pay(paymentRequest);

        assertThat(paymentRequest.getFare().fare()).isEqualTo(1_000);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    private Line.SectionBuilder createSectionBuilder(Station upStation, Station downStation, Distance distance, Duration duration) {
        return new Line.SectionBuilder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }
}