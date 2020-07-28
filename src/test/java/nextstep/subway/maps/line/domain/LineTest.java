package nextstep.subway.maps.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;


class LineTest {

    @Test
    @DisplayName("지하철 노선에 추가 요금 값이 존재한다.")
    void hasExtraFare() {
        //given
        Line line = new Line("2호선", "yellow", LocalTime.now(), LocalTime.now(), 3, 300);

        //when
        int extraFare = line.getExtraFare();

        //then
        assertThat(extraFare).isNotNull();
    }

}