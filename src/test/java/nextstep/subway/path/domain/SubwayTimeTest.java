package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayTimeTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Line line;
    private SubwayTime subwayTime;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        line = new Line("2호선", "green", 0, LocalTime.of(5,0), LocalTime.of(23,0), 10);
        line.addSection(역삼역, 선릉역, 10, 5);
        line.addSection(강남역, 역삼역, 10, 7);
        subwayTime = new SubwayTime(line);
    }

    @Test
    @DisplayName("가장 빠른 출발 시간 구하기")
    void getNextStartTime() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 4, 5, 9, 3);

        // when
        LocalDateTime nextTime = subwayTime.getNextStartTime(역삼역, dateTime);

        // then
        assertThat(nextTime).isEqualTo(LocalDateTime.of(2021, 4, 5, 9, 7));
    }

    @Test
    @DisplayName("가장 빠른 출발 시간 구하기 - 다음날")
    void getNextStartTimeTomorrow() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 4, 5, 23, 8);

        // when
        LocalDateTime nextTime = subwayTime.getNextStartTime(역삼역, dateTime);

        // then
        assertThat(nextTime).isEqualTo(LocalDateTime.of(2021, 4, 6, 5, 0));
    }

    @Test
    @DisplayName("도착 시간 구하기")
    void getArriveTime() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 4, 5, 9, 3);

        // when
        LocalDateTime arriveTime = subwayTime.getArriveTime(역삼역, 선릉역, dateTime);

        // then
        assertThat(arriveTime).isEqualTo(LocalDateTime.of(2021, 4, 5, 9, 12));
    }

    @Test
    @DisplayName("도착 시간 구하기 - 다음날")
    void getArriveTimeTomorrow() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 4, 5, 23, 8);

        // when
        LocalDateTime arriveTime = subwayTime.getArriveTime(역삼역, 선릉역, dateTime);

        // then
        assertThat(arriveTime).isEqualTo(LocalDateTime.of(2021, 4, 6, 5, 5));
    }
}
