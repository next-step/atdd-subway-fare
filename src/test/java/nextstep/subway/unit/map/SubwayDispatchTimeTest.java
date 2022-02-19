package nextstep.subway.unit.map;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.map.SubwayDispatchTime;

@DisplayName("SubwayDispatchTime 테스트")
public class SubwayDispatchTimeTest {
    @DisplayName("운행시간 내에서 계산할경우 입력한 출발 시간으로 도착 시간을 계산한다.")
    @CsvSource({
        "06:00,07:04,07:34", // 7시 20분에 타서 34분에 도착 한다.
        "06:03,07:04,07:37", // 7시 23분에 타서 37분에 도착 한다.
        "06:06,07:04,07:20" // 7시 6분에 타서 40분에 도착 한다.
    })
    @ParameterizedTest
    void findArrivalTimeCase1(LocalTime startTime, LocalTime takeTime, LocalTime expectTime) {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            startTime, LocalTime.of(23, 0), LocalTime.of(0, 20)
        );

        // When
        List<Integer> durations = Arrays.asList(10, 4);
        LocalDateTime takeDateTime = LocalDateTime.of(
            LocalDate.of(2022, 1, 1), takeTime
        );
        LocalDateTime answer = subwayDispatchTime.findArrivalTime(takeDateTime, durations);

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.of(2022, 1, 1), expectTime
        );
        assertThat(answer).isEqualTo(expect);
    }

    @DisplayName("첫차 시간 이전일경우 첫차 시간을 기준으로 도착 시간을 계산한다.")
    @Test
    void findArrivalTimeCase3() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalTime(
            LocalDateTime.of(LocalDate.of(2022, 1, 2),
                             LocalTime.of(4, 0)
            ),
            Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.of(2022, 1, 2),
            LocalTime.of(6, 15)
        );
        assertThat(answer).isEqualTo(expect);
    }

    @DisplayName("도착 시간이 막차 시간을 넘기면 다음날 첫차 시간을 기준으로 도착 시간을 계산한다.")
    @Test
    void findArrivalTimeCase4() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(21, 0), LocalTime.of(1, 0)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalTime(
            LocalDateTime.of(
                LocalDate.of(2022, 1, 3),
                LocalTime.of(20, 30)
            ),
            Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.of(2022, 1, 4),
            LocalTime.of(6, 15)
        );
        assertThat(answer).isEqualTo(expect);
    }
}
