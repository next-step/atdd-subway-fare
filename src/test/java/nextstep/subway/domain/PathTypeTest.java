package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {

    @DisplayName("경로조회_타입에_따라_사용하는 가중치가 달라진다.")
    @ParameterizedTest
    @CsvSource({"DURATION, 5", "DISTANCE, 1"})
    void getWight(String pathTypeString, int wight) {
        PathType pathType = PathType.valueOf(pathTypeString);
        Section section = new Section(null, null, null, 1, 5);

        assertThat(pathType.getWight(section)).isEqualTo(wight);
    }
}