package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    /**
     *       3m      4m     3m
     * 서초역---강남역---역삼역---선릉역 (이호선)
     */
    @DisplayName("환승 없는 경로의 도착시간을 조회한다.")
    @Test
    void getArrivalTime() {
        Station 서초역 = new Station("서초역");
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");

        Line 이호선 = new Line("이호선", "yellow", 500, LocalTime.of(5, 0), LocalTime.of(23, 0), 10);
        이호선.addSection(서초역, 강남역, 9, 3);
        이호선.addSection(강남역, 역삼역, 8, 4);
        이호선.addSection(역삼역, 선릉역, 15, 3);
        Path path = new Path(new Sections(이호선.getSections()));

        LocalDateTime arrivalTime = path.getArrivalTime(LocalDateTime.of(2023, 3, 6, 10, 0));

        assertThat(arrivalTime).isEqualTo(LocalDateTime.of(2023, 3, 6, 10, 10));
    }

    /**
     *       3m      4m     3m
     * 서초역---강남역---역삼역---선릉역 (이호선)
     *           |
     *           |4m
     *         양재역
     *         (신분당선)
     */
    @DisplayName("환승 있는 경로의 도착시간을 조회한다.")
    @Test
    void getArrivalTime2() {
        Station 서초역 = new Station("서초역");
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Station 양재역 = new Station("양재역");

        Line 이호선 = new Line("이호선", "yellow", 500, LocalTime.of(5, 0), LocalTime.of(23, 0), 10);
        Section section1 = 이호선.addSection(서초역, 강남역, 9, 3);
        Section section2 = 이호선.addSection(강남역, 역삼역, 8, 4);
        Section section3 = 이호선.addSection(역삼역, 선릉역, 15, 3);

        Line 신분당선 = new Line("신분당선", "red", 800, LocalTime.of(5, 0), LocalTime.of(23, 0), 20);
        Section section4 = 신분당선.addSection(강남역, 양재역, 9, 4);
        Path path = new Path(new Sections(List.of(section1, section4)));

        LocalDateTime arrivalTime = path.getArrivalTime(LocalDateTime.of(2023, 3, 6, 10, 0));

        assertThat(arrivalTime).isEqualTo(LocalDateTime.of(2023, 3, 6, 10, 24));
    }
}
