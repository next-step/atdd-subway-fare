package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import nextstep.subway.domain.exception.PathTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("경로 타입 관련 기능")
class PathTypeTest {

    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        this.line = new Line();
        this.upStation = new Station();
        this.downStation = new Station();
    }

    @DisplayName("경로 타입 거리를 기준으로 가중치를 가져온다.")
    @ParameterizedTest(name = "distance : {0}")
    @ValueSource(ints = {1, 100, 9999})
    void distance(int distance) {
        Section section = new Section(line, upStation, downStation, distance, 100);

        assertThat(PathType.DISTANCE.getStrategy().apply(section)).isEqualTo(distance);
    }

    @DisplayName("경로 타입 시간을 기준으로 가중치를 가져온다.")
    @ParameterizedTest(name = "duration : {0}")
    @ValueSource(ints = {1, 100, 9999})
    void duraiton(int duration) {
        Section section = new Section(line, upStation, downStation, 100, duration);

        assertThat(PathType.DURATION.getStrategy().apply(section)).isEqualTo(duration);
    }

    private static Stream<Arguments> provideForFindByName() {
        return Stream.of(
                Arguments.of("DISTANCE", PathType.DISTANCE)
        );
    }

    @DisplayName("이름을 기준으로 경로 타입을 찾는다.")
    @MethodSource("provideForFindByName")
    @ParameterizedTest(name = "name : {0}")
    void findByName(String name, PathType pathType) {
        assertThat(PathType.findBy(name)).isEqualTo(pathType);
    }

    @DisplayName("존재하지 않는 타입 이름이면 예외 처리한다.")
    @Test
    void findByNameNotFound() {
        assertThatThrownBy(() -> PathType.findBy("존재하지 않는 타입 이름")).isInstanceOf(PathTypeNotFoundException.class);
    }
}
