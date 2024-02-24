package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PathTypeTest {

    @Test
    void PathType_문자열에_매칭되는_PathType_enum을_반환한다() {
        assertThat(PathType.of("DISTANCE")).isEqualTo(DISTANCE);
        assertThat(PathType.of("DURATION")).isEqualTo(DURATION);
        assertThat(PathType.of("UNKNOWN")).isEqualTo(DISTANCE);
    }

    @Test
    void PathType_listOf는_모든_PathType을_반환한다() {
        List<PathType> pathTypes = PathType.listOf();

        // 리스트 크기 검증
        assertThat(pathTypes).hasSize(2)
                .contains(DISTANCE, DURATION);
    }

}
