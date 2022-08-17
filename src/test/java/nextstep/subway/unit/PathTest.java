package nextstep.subway.unit;

import nextstep.subway.domain.DiscountPolicy;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.FarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 조회 테스트")
class PathTest {

    private Station 지하철A역 = new Station("지하철A역");
    private Station 지하철B역 = new Station("지하철B역");

    private Line 신분당선 = new Line("신분당선", "red", 900);

    @DisplayName("기본 경로 조회")
    @Test
    void defaultFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 9, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        final int 요금_1250원 = DEFAULT_FARE;
        final int 거리_9km = 9;
        경로_검증(path, 요금_1250원, 거리_9km);
    }

    @DisplayName("10km 초과 경로 조회")
    @Test
    void over10KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 10, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        final int 요금_1350원 = DEFAULT_FARE + 100;
        final int 거리_10km = 10;
        경로_검증(path, 요금_1350원, 거리_10km);
    }

    @DisplayName("49km 경로 조회")
    @Test
    void over49KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 49, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        final int 요금_2050원 = DEFAULT_FARE + 800;
        final int 거리_49km = 49;
        경로_검증(path, 요금_2050원, 거리_49km);
    }

    @DisplayName("50km 초과 경로 조회")
    @Test
    void over50KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 50, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        final int 요금_2150원 = DEFAULT_FARE + 800 + 100;
        final int 거리_50km = 50;
        경로_검증(path, 요금_2150원, 거리_50km);
    }

    @DisplayName("신분당선 경유 기본 경로 조회")
    @Test
    void defaultFareWith_신분당선() {
        Section AB구간 = new Section(신분당선, 지하철A역, 지하철B역, 9, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections, new Fare(sections.totalDistance(), 신분당선.getAdditionalFare(), DiscountPolicy.ADULT));

        final int 요금_2150원 = DEFAULT_FARE + 신분당선.getAdditionalFare();
        final int 거리_9km = 9;
        경로_검증(path, 요금_2150원, 거리_9km);
    }

    @DisplayName("신분당선 경유 10km 초과 경로 조회")
    @Test
    void over10KmFareWith_신분당선() {
        Section AB구간 = new Section(신분당선, 지하철A역, 지하철B역, 10, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections, new Fare(sections.totalDistance(), 신분당선.getAdditionalFare(), DiscountPolicy.ADULT));

        final int 요금_2250원 = DEFAULT_FARE + 신분당선.getAdditionalFare() + 100;
        final int 거리_10km = 10;
        경로_검증(path, 요금_2250원, 거리_10km);
    }

    @DisplayName("신분당선 경유 49km 경로 조회")
    @Test
    void over49KmFareWith_신분당선() {
        Section AB구간 = new Section(신분당선, 지하철A역, 지하철B역, 49, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections, new Fare(sections.totalDistance(), 신분당선.getAdditionalFare(), DiscountPolicy.ADULT));

        final int 요금_2950원 = DEFAULT_FARE + 신분당선.getAdditionalFare() + 800;
        final int 거리_49km = 49;
        경로_검증(path, 요금_2950원, 거리_49km);
    }

    @DisplayName("신분당선 경유 50km 초과 경로 조회")
    @Test
    void over50KmFareWith_신분당선() {
        Section AB구간 = new Section(신분당선, 지하철A역, 지하철B역, 50, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections, new Fare(sections.totalDistance(), 신분당선.getAdditionalFare(), DiscountPolicy.ADULT));

        final int 요금_3050원 = DEFAULT_FARE + 신분당선.getAdditionalFare() + 800 + 100;
        final int 거리_50km = 50;
        경로_검증(path, 요금_3050원, 거리_50km);
    }

    private void 경로_검증(Path path, int fare, int distance) {
        assertThat(path.getFare()).isEqualTo(fare);
        assertThat(path.extractDistance()).isEqualTo(distance);
        assertThat(path.extractDuration()).isEqualTo(3);
        assertThat(path.getStations()).containsExactly(지하철A역, 지하철B역);
    }
}