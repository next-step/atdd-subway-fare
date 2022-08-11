package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {

    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(null, new Station("강남역"), new Station("역삼역"), 10, 3);
    }

    @Test
    @DisplayName("DISTANCE 일 경우 PathType.DISTANCE 를 반환한다.")
    void distance() {
        PathType distanceType = PathType.of("DISTANCE");

        assertThat(distanceType == PathType.DISTANCE).isTrue();
    }

    @Test
    @DisplayName("DURATION 일 경우 PathType.DURATION 를 반환한다.")
    void duration() {
        PathType durationType = PathType.of("DURATION");

        assertThat(durationType == PathType.DURATION).isTrue();
    }

    @Test
    @DisplayName("PathType 이 DISTANCE 일 경우 가중치의 전략패턴은 거리이다.")
    void distance_weight_strategy() {
        PathType distanceType = PathType.of("DISTANCE");

        int weight = distanceType.weightStrategy().weight(section);

        assertThat(weight).isEqualTo(section.getDistance());
    }

    @Test
    @DisplayName("PathType 이 DURATION 일 경우 가중치의 전략패턴은 소요 시간이다.")
    void duration_weight_strategy() {
        PathType durationType = PathType.of("DURATION");

        int weight = durationType.weightStrategy().weight(section);

        assertThat(weight).isEqualTo(section.getDuration());
    }
}