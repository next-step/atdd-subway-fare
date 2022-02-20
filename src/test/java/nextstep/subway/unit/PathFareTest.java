package nextstep.subway.unit;

import nextstep.subway.domain.PathFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

public class PathFareTest {

    @DisplayName("거리별 요금 테스트")
    @ParameterizedTest
    @CsvSource(value = {"1,1250","10,1250","11,1350","50,2050","51,2150","58,2150","59,2250"})
    void fare(int distance, int fare) {
        int result = PathFare.extractFare(distance);

        assertThat(result).isEqualTo(fare);
    }
}
