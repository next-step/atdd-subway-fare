package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    @DisplayName("환승 없는 경로의 도착시간을 조회한다.")
    @Test
    void getArrivalTime() {
        Line 이호선 = new Line("이호선", "yellow", 500, LocalTime.of(5, 0), LocalTime.of(23, 0), 10);
        Section section1 = new Section(이호선, new Station("강남역"), new Station("역삼역"), 8, 4);
        Section section2 = new Section(이호선, new Station("역삼역"), new Station("선릉역"), 15, 3);
        Path path = new Path(new Sections(List.of(section1, section2)));

        LocalDateTime arrivalTime = path.getArrivalTime(LocalDateTime.of(2023, 3, 6, 10, 0));

        assertThat(arrivalTime).isEqualTo(LocalDateTime.of(2023, 3, 6, 10, 7));
    }
}
