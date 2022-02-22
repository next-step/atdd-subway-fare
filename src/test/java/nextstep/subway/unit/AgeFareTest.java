package nextstep.subway.unit;

import nextstep.subway.domain.AgeFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class AgeFareTest {

    @DisplayName("어린이 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    void findChild(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.CHILD_FARE);
    }

    @DisplayName("청소년 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 16, 18})
    void findYouth(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.YOUTH_FARE);
    }

    @DisplayName("일반 요금제를 찾는다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 25, 99})
    void findGeneral(int age) {
        // when
        AgeFare findChild = AgeFare.findAgeFareType(age);

        // then
        assertThat(findChild).isEqualTo(AgeFare.GENERAL_FARE);
    }
}
