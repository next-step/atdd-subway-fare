package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.domain.Fare.BASIC_FARE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareTest {
    @DisplayName("기본운임(10㎞ 이내) : 기본운임 1,250원")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void 기본운임_테스트(int distance) {
        Fare fare = new Fare(distance);

        assertThat(fare.getFare()).isEqualTo(BASIC_FARE);
    }

    @DisplayName("이용 거리초과 시 추가운임 부과 : 10km초과∼50km까지(5km마다 100원)")
    @ParameterizedTest
    @CsvSource({"11,1350", "50,2050"})
    void 추가운임_50km_이하_테스트(int distance, int expected) {
        Fare fare = new Fare(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @DisplayName("이용 거리초과 시 추가운임 부과 : 50km초과 시 (8km마다 100원)")
    @ParameterizedTest(name = "거리: {0}Km")
    @CsvSource({"51,2150", "100,2750"})
    void 추가운임_50km_초과_테스트(int distance, int expected) {
        Fare fare = new Fare(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }
}
