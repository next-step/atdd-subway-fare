package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @CsvSource({"1, 1250", "9, 1250"})
    @ParameterizedTest(name = "10㎞ 이내면 1Km 마다 기본운임(1,250원) 이 부과된다. (거리: {0}, 요금: {1} )")
    void calculateDefaultFare(Long distance, Long fare) {
        Path path = new Path(Collections.emptyList(), distance, 1L);

        Long actual = path.fare();
        assertThat(actual).isEqualTo(fare);
    }

}
