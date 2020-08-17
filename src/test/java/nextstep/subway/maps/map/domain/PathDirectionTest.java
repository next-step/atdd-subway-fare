package nextstep.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.maps.line.domain.LineStation;

public class PathDirectionTest {

    @DisplayName("source의 역 아이디를 활용하여 상행 또는 하행 방향을 결정할 수 있다.")
    @Test
    void 소스_아이디로_방향을_결정한다() {
        // given
        LineStation lineStation = new LineStation(1L, 2L, 3, 3);

        // when
        PathDirection upbound = PathDirection.findBySourceId(lineStation, lineStation.getPreStationId());
        PathDirection downbound = PathDirection.findBySourceId(lineStation, lineStation.getStationId());

        // then
        assertAll(
            () -> assertThat(upbound).isEqualTo(PathDirection.UPBOUND),
            () -> assertThat(downbound).isEqualTo(PathDirection.DOWNBOUND)
        );
    }
}
