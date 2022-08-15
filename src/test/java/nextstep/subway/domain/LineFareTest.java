package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineFareTest {

    @DisplayName("추가요금을 구할 수 있다.")
    @Test
    void getLineFare() {
        //given
        Line 이호선 = new Line("이호선", "초록색");
        Line 신분당선 = new Line("신분당선", "빨간색", 900);
        LineFare 요금 = new LineFare(List.of(이호선, 신분당선));

        //when
        int result = 요금.get();

        //then
        assertThat(result).isEqualTo(900);
    }
}