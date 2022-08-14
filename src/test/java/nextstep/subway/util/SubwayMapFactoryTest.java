package nextstep.subway.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SubwayMapFactoryTest {

    private SubwayMapFactory subwayMapFactory;

    @BeforeEach
    void init() {
        HashSet<SubwayMap> subwayMaps = new HashSet<>(List.of(new ShortestDistanceSubwayMap(), new ShortestDurationSubwayMap()));
        subwayMapFactory = new SubwayMapFactory(subwayMaps);
    }

    @DisplayName("타입에 맞는 SubwayMap을 반환한다.")
    @Test
    void subwayMap() {
        SubwayMap distance = subwayMapFactory.subwayMap("DISTANCE");
        SubwayMap duration = subwayMapFactory.subwayMap("DURATION");

        assertAll(
                () -> assertThat(distance).isInstanceOf(ShortestDistanceSubwayMap.class),
                () -> assertThat(duration).isInstanceOf(ShortestDurationSubwayMap.class)
        );
    }

    @DisplayName("존재하지 않는 타입의 SubwayMap을 요청할 경우 예외를 발생시킨다.")
    @Test
    void subwayMapNonExistentType() {
        assertThatThrownBy(() -> subwayMapFactory.subwayMap("EXAMPLE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
