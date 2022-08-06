package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SectionsTest {

    private Sections sections;

    @BeforeEach
    void setUp() {
        Line line = new Line("2호선", "green");
        Station 사당역 = new Station("사당역");
        Station 방배역 = new Station("방배역");
        Station 논현역 = new Station("논현역");

        sections = new Sections(List.of(
                new Section(line, 사당역, 방배역, 5, 10),
                new Section(line, 사당역, 논현역, 5, 10)
        ));
    }

    @Test
    @DisplayName("총 소요시간의 합이 반환된다.")
    void totalDurationTest() {
        assertThat(sections.totalDuration()).isEqualTo(20);
    }

    @Test
    @DisplayName("총 거리의 합이 반환된다.")
    void totalDistanceTest() {
        assertThat(sections.totalDistance()).isEqualTo(10);
    }

}