package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.LineStation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathDirectionTest {

    @Test
    void findBySourceId() {
        //given
        LineStation lineStation = new LineStation(1L, 2L, 3, 3);

        //when
        PathDirection forward = PathDirection.findBySourceId(lineStation, lineStation.getPreStationId());
        PathDirection reverse = PathDirection.findBySourceId(lineStation, lineStation.getStationId());

        //then
        assertThat(forward).isEqualTo(PathDirection.FORWARD);
        assertThat(reverse).isEqualTo(PathDirection.REVERSE);
    }

}