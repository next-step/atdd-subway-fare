package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static nextstep.support.entity.Formatters.DATE_TIME_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("도착시간 관련 테스트")
class ArrivalTimeTest {
    Station 지하철A = new Station("지하철A");
    Station 지하철B = new Station("지하철B");
    Station 지하철C = new Station("지하철C");
    Station 지하철D = new Station("지하철D");
    Station 지하철E = new Station("지하철E");

    Line 노선A;
    Line 노선B;

    Section 구간AB;
    Section 구간BC;
    Section 구간CD;
    Section 구간BE;

    @BeforeEach
    void setUp() {
        노선A = Line.builder()
                .name("노선A")
                .color("red")
                .startTime(LocalTime.of(5, 0))
                .endTime(LocalTime.of(23, 0))
                .intervalTime(10)
                .build();
        노선A.addSection(지하철A, 지하철B, 5, 3);
        노선A.addSection(지하철B, 지하철C, 7, 4);
        노선A.addSection(지하철C, 지하철D, 4, 3);

        노선B = Line.builder()
                .name("노선B")
                .color("orange")
                .startTime(LocalTime.of(5, 0))
                .endTime(LocalTime.of(23, 0))
                .intervalTime(20)
                .build();
        노선B.addSection(지하철B, 지하철E, 6, 4);

        구간AB = 노선A.getSections().get(0);
        구간BC = 노선A.getSections().get(1);
        구간CD = 노선A.getSections().get(2);
        구간BE = 노선B.getSections().get(0);
    }

    @DisplayName("10:00 기준으로 환승이 없는 경로 도착시간 구하기")
    @Test
    void noneTransfer() {
        // given
        List<Line> lines = List.of(노선A, 노선B);
        List<Section> sections = List.of(구간BC, 구간CD);
        String time = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)).format(DATE_TIME_PATH);
        ArrivalTime arrivalTime = new ArrivalTime(sections, lines, time);

        // when
        LocalDateTime value = arrivalTime.value();

        // then
        assertThat(value).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 10)));
    }

    @DisplayName("10:00 기준으로 환승이 있는 경로 도착시간 구하기")
    @Test
    void withTransfer() {
        // given
        List<Line> lines = List.of(노선A, 노선B);
        List<Section> sections = List.of(구간AB, 구간BE);
        String time = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)).format(DATE_TIME_PATH);
        ArrivalTime arrivalTime = new ArrivalTime(sections, lines, time);

        // when
        LocalDateTime value = arrivalTime.value();

        // then
        assertThat(value).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 24)));
    }

    @DisplayName("막차 시간(23:00) 기준으로 환승이 없는 경로 도착시간 구하기")
    @Test
    void noneTransferAtEndTime() {
        // given
        List<Line> lines = List.of(노선A, 노선B);
        List<Section> sections = List.of(구간BC, 구간CD);
        String time = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)).format(DATE_TIME_PATH);
        ArrivalTime arrivalTime = new ArrivalTime(sections, lines, time);

        // when
        LocalDateTime value = arrivalTime.value();

        // then
        assertThat(value).isEqualTo(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(5, 10)));
    }

    @DisplayName("막차 시간(23:00) 기준으로 환승이 있는 경로 도착시간 구하기")
    @Test
    void withTransferAtEndTime() {
        // given
        List<Line> lines = List.of(노선A, 노선B);
        List<Section> sections = List.of(구간AB, 구간BE);
        String time = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)).format(DATE_TIME_PATH);
        ArrivalTime arrivalTime = new ArrivalTime(sections, lines, time);

        // when
        LocalDateTime value = arrivalTime.value();

        // then
        assertThat(value).isEqualTo(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(5, 24)));
    }
}