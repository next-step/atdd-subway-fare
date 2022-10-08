package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    Line 일호선;
    Station 잠실역;
    Station 선릉역;
    Station 강남역;
    Station 논현역;
    Station 신논현역;
    Station 고속터미널역;

    @BeforeEach
    void setUp() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(23, 0);
        일호선 = new Line("일호선", "green", 1000, start, end, 10);
        잠실역 = new Station(1L, "잠실역");
        선릉역 = new Station(2L, "선릉역");
        강남역 = new Station(3L, "강남역");
        논현역 = new Station(4L, "논현역");
        신논현역 = new Station(5L, "신논현역");
        고속터미널역 = new Station(6L, "고속터미널역");

        일호선.addSection(잠실역, 선릉역, 10, 5);
        일호선.addSection(선릉역, 강남역, 10, 5);
        일호선.addSection(강남역, 논현역, 10, 5);
        일호선.addSection(논현역, 신논현역, 10, 5);
        일호선.addSection(신논현역, 고속터미널역, 10, 5);
    }

    @DisplayName("하행일 때 탑승 시간을 계산한다.")
    @Test
    void getStopTime() {
        LocalDateTime current = LocalDateTime.of(2022, 10, 4, 16, 1);

        LocalDateTime stopTime = 일호선.getStopTime(current, 선릉역, false);

        assertThat(stopTime).isEqualTo(LocalDateTime.of(2022, 10, 4, 16, 5));
    }

    @DisplayName("하행일 때 당일에 탑승할 수 없는 경우 탑승 시간을 계산한다.")
    @Test
    void getStopTimeNextDay() {
        LocalDateTime current = LocalDateTime.of(2022, 10, 4, 23, 59);

        LocalDateTime stopTime = 일호선.getStopTime(current, 선릉역, false);

        assertThat(stopTime).isEqualTo(LocalDateTime.of(2022, 10, 5, 9, 0));
    }

    @DisplayName("상행일 때 탑승 시간을 계산한다.")
    @Test
    void getStopTimeReverse() {
        LocalDateTime current = LocalDateTime.of(2022, 10, 4, 16, 1);

        LocalDateTime stopTime = 일호선.getStopTime(current, 선릉역, true);

        assertThat(stopTime).isEqualTo(LocalDateTime.of(2022, 10, 4, 16, 10));
    }

    @DisplayName("상행일 때 당일에 탑승할 수 없는 경우 탑승 시간을 계산한다.")
    @Test
    void getStopTimeReverseNextDay() {
        LocalDateTime current = LocalDateTime.of(2022, 10, 4, 23, 59);

        LocalDateTime stopTime = 일호선.getStopTime(current, 선릉역, true);

        assertThat(stopTime).isEqualTo(LocalDateTime.of(2022, 10, 5, 9, 0));
    }
}
