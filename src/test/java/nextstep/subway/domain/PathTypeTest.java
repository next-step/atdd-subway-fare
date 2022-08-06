package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {

    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(new Line("2호선", "green"), new Station("사당역"), new Station("방배역"), 5, 10);
    }

    @Test
    @DisplayName("DISTANCE 인경우 거리를 반환한다.")
    void parseWeightDistanceTest() {
        assertThat(DISTANCE.parseWeight(section)).isEqualTo(section.getDistance());
    }

    @Test
    @DisplayName("DURATION 인경우 소요시간을 반환환다.")
    void parseWeightDurationTest() {
        assertThat(DURATION.parseWeight(section)).isEqualTo(section.getDuration());
    }
}