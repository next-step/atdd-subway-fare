package nextstep.subway.utils;

import static nextstep.subway.utils.StringDateTimeConverter.convertDateTimeToString;
import static nextstep.subway.utils.StringDateTimeConverter.convertLineTimeToDateTime;
import static nextstep.subway.utils.StringDateTimeConverter.convertStringToDateTime;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class StringDateTimeConverterTest {

    @Test
    void convertDateTimeToStringTest() {
        LocalDateTime time = LocalDateTime.of(2022, 02, 20, 06, 00);
        String timeString = convertDateTimeToString(time);

        assertThat(timeString).isEqualTo("202202200600");
    }

    @Test
    void dateTimeComparisonTest() {
        LocalDateTime bigTime = convertStringToDateTime("202202201604");
        LocalDateTime smallTime = convertStringToDateTime("202202201603");

        assertThat(bigTime.isAfter(smallTime)).isTrue();
    }

    @Test
    void convertLineTimeToDateTimeTest() {
        LocalDateTime findPathTime = convertStringToDateTime("202202200600");
        LocalDateTime dateTime = convertLineTimeToDateTime(findPathTime, "1630");

        assertThat(dateTime.getYear()).isEqualTo(2022);
        assertThat(dateTime.getMonthValue()).isEqualTo(02);
        assertThat(dateTime.getHour()).isEqualTo(16);
        assertThat(dateTime.getMinute()).isEqualTo(30);
    }

}