package nextstep.subway.util.subwaymap;

import nextstep.subway.util.subwaymap.PathType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTypeTest {
    @DisplayName("이름에 맞는 타입을 찾아 반환한다.")
    @CsvSource(value = {"DISTANCE:DISTANCE", "DURATION:DURATION"}, delimiter = ':')
    @ParameterizedTest
    void find(String type, PathType pathType) {
        assertThat(PathType.find(type)).isEqualTo(pathType);
    }

    @DisplayName("존재하지 않는 타입을 찾을 경우 예외를 발생시킨다.")
    @Test
    void findNonExistentPathType() {
        assertThatThrownBy(() -> PathType.find("example"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
