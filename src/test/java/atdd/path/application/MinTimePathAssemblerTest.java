package atdd.path.application;

import atdd.path.application.dto.MinTimePathAssembler;
import atdd.path.application.dto.MinTimePathResponseView;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MinTimePathAssemblerTest {
    @Test
    public void assembler() {
        List<Line> lines = Arrays.asList(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
        List<Station> pathStations = Arrays.asList(TEST_STATION_11, TEST_STATION_12, TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4);

        MinTimePathResponseView responseView = new MinTimePathAssembler(lines, pathStations, LocalDateTime.of(2020, 3, 13, 9, 27, 44)).assemble();

        LocalDateTime localDateTime = LocalDateTime.of(2020, 3, 13, 10, 0, 0, 0);

        assertThat(responseView.getLines().size()).isEqualTo(2);
        assertThat(responseView.getDistance()).isEqualTo(50);
        assertThat(responseView.getArriveBy()).isEqualTo(localDateTime);
    }
}