package nextstep.study.unit;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeTest {
    @Test
    void normal() {
        LocalTime startTime = LocalTime.of(05, 30);
        LocalTime endTime = LocalTime.of(23, 30);

        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30));

        LocalDateTime startDateTime = LocalDateTime.of(now.toLocalDate(), startTime);
        LocalDateTime endDateTime = LocalDateTime.of(now.toLocalDate(), endTime);

        boolean work = now.isAfter(startDateTime) && now.isBefore(endDateTime);

        assertThat(work).isTrue();
    }
}
