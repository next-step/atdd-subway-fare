package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static nextstep.subway.domain.fare.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("10Km 이내 거리는 기본 요금을 받는다.")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest(name = "거리: {0}Km")
    void defaultFare(int distance) {
        Fare fare = new Fare(distance, 0);

        assertThat(fare.cost()).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"12,1350", "16,1450", "30,1_650"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra10KmFare(int distance, int expected) {
        Fare fare = new Fare(distance, 0);

        assertThat(fare.cost()).isEqualTo(expected);
    }

    @DisplayName("50Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"66,2250", "75,2450"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra50KmFare(int distance, int expected) {
        Fare fare = new Fare(distance, 0);

        assertThat(fare.cost()).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 있는 노선인 경우 노선 추가 요금이 부과된다.")
    @Test
    void additionalFare() {
        Line 이호선 = createLine("2호선", 500);
        Line 삼호선 = createLine("3호선", 1000);

        Station 역삼역 = createStation("역삼역");
        Station 강남역 = createStation("강남역");
        Station 교대역 = createStation("교대역");
        Station 남부터미널역 = createStation("남부터미널역");

        Section 역삼역_강남역 = createSection(역삼역, 강남역, 10);
        Section 강남역_교대역 = createSection(강남역, 교대역, 10);
        Section 교대역_남부터미널역 = createSection(교대역, 남부터미널역, 10);
        Sections sections = new Sections(List.of(역삼역_강남역, 강남역_교대역, 교대역_남부터미널역));
        이호선.addSection(역삼역_강남역);
        이호선.addSection(강남역_교대역);
        삼호선.addSection(교대역_남부터미널역);

        Fare fare = new Fare(sections);
        assertThat(fare.cost()).isEqualTo(1_650 + 1_000);
    }


    private Station createStation(String name) {
        return new Station(name);
    }

    private Section createSection(Station upStation, Station downStation, int distance) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private Line createLine(String name, int additionalFare) {
        return new Line(name, "", additionalFare);
    }
}
