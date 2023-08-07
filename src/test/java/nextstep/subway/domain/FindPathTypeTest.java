package nextstep.subway.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindPathTypeTest {

    public static final String OTHER_NAME = "otherName";

    @DisplayName("타입 이름과 같은 해당 타입을 반환하고 없으면 거리 타입을 반환한다")
    @Test
    void find() {
        // given
        String distanceTypeName = FindPathType.DISTANCE.name();
        String durationTypeName = FindPathType.DURATION.name();

        // when,then
        Assertions.assertAll(
                () -> assertThat(FindPathType.find(distanceTypeName)).isEqualTo(FindPathType.DISTANCE),
                () -> assertThat(FindPathType.find(durationTypeName)).isEqualTo(FindPathType.DURATION),
                () -> assertThat(FindPathType.find(OTHER_NAME)).isEqualTo(FindPathType.DISTANCE)
        );
    }
}
