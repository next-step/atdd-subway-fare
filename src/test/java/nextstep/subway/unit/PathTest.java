package nextstep.subway.unit;

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

    @DisplayName("기본 요금 조회")
    @Test
    void defaultFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 9, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        int fare = path.getFare();

        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10km 초과 요금 조회")
    @Test
    void over10KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 10, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        int fare = path.getFare();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("49km 요금 조회")
    @Test
    void over49KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 49, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        int fare = path.getFare();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("50km 초과 요금 조회")
    @Test
    void over50KmFare() {
        Section AB구간 = new Section(null, 지하철A역, 지하철B역, 50, 3);
        Sections sections = new Sections(List.of(AB구간));
        Path path = new Path(sections);

        int fare = path.getFare();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}