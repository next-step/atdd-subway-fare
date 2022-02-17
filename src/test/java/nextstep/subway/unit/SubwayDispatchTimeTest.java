package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import nextstep.subway.domain.SubwayDispatchTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SubwayDispatchTime 테스트")
public class SubwayDispatchTimeTest {
    @DisplayName("운행시간 내에서 계산할경우")
    @Test
    void takeCase1() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalDateTime(
            LocalTime.of(7, 0), Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.now(), LocalTime.of(7, 15)
        );
        assertThat(answer).isEqualTo(expect);
    }

    @DisplayName("첫차 시간 이전에 계산할경우")
    @Test
    void takeCase2() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalDateTime(
            LocalTime.of(6, 0), Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.now(), LocalTime.of(6, 15)
        );
        assertThat(answer).isEqualTo(expect);
    }

    @DisplayName("도착 시간이 막차 시간을 넘기면 다음날 첫차 시간을 갖는다.")
    @Test
    void takeCase3() {
        // Given
        SubwayDispatchTime subwayDispatchTime = new SubwayDispatchTime(
            LocalTime.of(6, 0), LocalTime.of(6, 12), LocalTime.of(0, 10)
        );

        // When
        LocalDateTime answer = subwayDispatchTime.findArrivalDateTime(
            LocalTime.of(6, 0), Arrays.asList(10, 5)
        );

        // Then
        LocalDateTime expect = LocalDateTime.of(
            LocalDate.now().plusDays(1), LocalTime.of(6, 5)
        );
        assertThat(answer).isEqualTo(expect);
    }
}
