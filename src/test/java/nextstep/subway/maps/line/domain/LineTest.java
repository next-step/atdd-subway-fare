package nextstep.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @DisplayName("지하철 노선에는 추가 요금 필드가 존재한다.")
    @Test
    void 지하철_노선에_추가요금이_존재한다() {
        // given
        Line line = new Line("2호선", "GREEN", LocalTime.now(), LocalTime.now(), 3, 400);

        // when
        assertThat(line.getExtraFare()).isNotNull();
    }
}
