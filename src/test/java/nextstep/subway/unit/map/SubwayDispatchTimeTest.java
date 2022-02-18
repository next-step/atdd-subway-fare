package nextstep.subway.unit.map;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import jdk.vm.ci.meta.Local;
import nextstep.subway.domain.map.SubwayDispatchTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SubwayDispatchTime 테스트")
public class SubwayDispatchTimeTest {
    @DisplayName("운행시간 내에서 계산할경우 입력한 출발 시간으로 도착 시간을 계산한다.")
    @Test
    void takeCase1() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalTime(
            LocalDateTime.of(
                LocalDate.of(2022, 1, 1),
                LocalTime.of(7, 0)
            ),
            Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.of(2022, 1, 1),
            LocalTime.of(7, 15)
        );
        assertThat(answer).isEqualTo(expect);
    }

    @DisplayName("첫차 시간 이전일경우 첫차 시간을 기준으로 도착 시간을 계산한다.")
    @Test
    void takeCase2() {
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
    void takeCase3() {
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
