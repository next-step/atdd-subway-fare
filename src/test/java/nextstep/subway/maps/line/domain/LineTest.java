package nextstep.subway.maps.line.domain;

import nextstep.subway.utils.SimpleLocalTimeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line("1호선", "BLUE", LocalTime.of(5, 30), LocalTime.of(23, 00), 5);
        LineStation lineStation1 = new LineStation(1L, null, 10, 10);
        LineStation lineStation2 = new LineStation(2L, null, 10, 10);
        LineStation lineStation3 = new LineStation(3L, null, 10, 10);
        LineStation lineStation4 = new LineStation(4L, null, 10, 10);
        LineStation lineStation5 = new LineStation(5L, null, 10, 10);
        line.addLineStation(lineStation1);
        line.addLineStation(lineStation2);
        line.addLineStation(lineStation3);
        line.addLineStation(lineStation4);
        line.addLineStation(lineStation5);
    }

    @DisplayName("기점에서 다음 열차 출발 시간을 계산한다.")
    @ParameterizedTest
    @CsvSource({"0810,0810", "1001,1005", "2330,2330"})
    public void calculateEarliestDepartureTimeTest(
            @ConvertWith(SimpleLocalTimeConverter.class) LocalTime time,
            @ConvertWith(SimpleLocalTimeConverter.class) LocalTime expected
    ) {
        // when
        LocalTime earliestDepartureTime = line.calculateForwardDepartureTime(time);

        // then
        assertThat(earliestDepartureTime).isEqualTo(expected);
    }

    @DisplayName("종점에서 다음 열차 출발 시간을 계산한다(역방향).")
    @ParameterizedTest
    @CsvSource({"0530,0610", "1001,1005", "0007,0010"})
    public void calculateEarliestReverseDepartureTimeTest(
            @ConvertWith(SimpleLocalTimeConverter.class) LocalTime time,
            @ConvertWith(SimpleLocalTimeConverter.class) LocalTime expected
    ) {
        // when
        LocalTime earliestDepartureTime = line.calculateReverseDepartureTime(time);

        // then
        assertThat(earliestDepartureTime).isEqualTo(expected);
    }

    @DisplayName("술 마시다 열차가 끊겼을 때 테스트")
    @Test
    public void timesOvertimesSubwayOperatingTest() {
        // given
        LocalTime time = LocalTime.of(3, 30);

        // when & then
        // 기점 기준
        assertThatThrownBy(() -> line.calculateForwardDepartureTime(time))
                .isInstanceOf(SubwayNotOperatingTimeException.class);

        // 종점 기준
        assertThatThrownBy(() -> line.calculateReverseDepartureTime(time))
                .isInstanceOf(SubwayNotOperatingTimeException.class);
    }
}