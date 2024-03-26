package nextstep.subway.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;

    @DisplayName("거리에 따른 요금 조회")
    @Test
    void distanceFare() {
        assertThat(FareCalculator.getDistanceFare(10)).isEqualTo(BASIC_FARE);
        assertThat(FareCalculator.getDistanceFare(50)).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8);
        assertThat(FareCalculator.getDistanceFare(51)).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8 + ADDITIONAL_FARE * 1);
    }

    @DisplayName("노선별 추가 요금 조회")
    @Test
    void lineAdditionalFare() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 5, 8, 500);
        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 7, 11, 900);
        List<Section> sections = List.of(이호선, 신분당선).stream()
                .flatMap(line -> line.getSections().get().stream())
                .collect(Collectors.toList());

        assertThat(FareCalculator.getLineAdditionalFare(List.of(교대역, 강남역, 양재역), sections)).isEqualTo(900);
    }
}
