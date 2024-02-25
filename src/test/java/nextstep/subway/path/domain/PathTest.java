package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @CsvSource({"1, 1250", "10, 1250"})
    @ParameterizedTest(name = "10㎞ 이내면 1Km 마다 기본운임(1,250원) 이 부과된다. (거리: {0}, 요금: {1} )")
    void calculateDefaultFare(Long distance, Long fare) {
        Path path = new Path(Collections.emptyList(), distance, 1L);

        Long actual = path.fare();
        assertThat(actual).isEqualTo(fare);
    }

    @CsvSource({"11, 1350", "50, 2050"})
    @ParameterizedTest(name = "10km 초과 ∼ 50km 까지 5km 마다 추가운임(100원) 부과된다. (거리: {0}, 요금: {1} )")
    void calculate10To50tFare(Long distance, Long fare) {
        Path path = new Path(Collections.emptyList(), distance, 1L);

        Long actual = path.fare();
        assertThat(actual).isEqualTo(fare);
    }

    @CsvSource({"50, 2050", "51, 2150", "56, 2150", "59, 2250"})
    @ParameterizedTest(name = "50km 초과 시 8km 마다 추가운임(100원) 부과된다. (거리: {0}, 요금: {1} )")
    void calculate50OverFare(Long distance, Long fare) {
        Path path = new Path(Collections.emptyList(), distance, 1L);

        Long actual = path.fare();
        assertThat(actual).isEqualTo(fare);
    }

}
