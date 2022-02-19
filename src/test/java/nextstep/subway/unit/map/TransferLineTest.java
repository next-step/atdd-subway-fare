package nextstep.subway.unit.map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SubwayDispatchTime;
import nextstep.subway.domain.map.TransferLine;

@DisplayName("TransferLine Test")
@ExtendWith(MockitoExtension.class)
public class TransferLineTest {
    @Mock
    private Line line;

    @DisplayName("환승 라인중 첫번째 역이 가지고 있는 노선 기준의 배차 시간 반환")
    @Test
    void firstDispatchTime() {
        // Given
        Station upStation = new Station();
        SubwayDispatchTime expect = new SubwayDispatchTime(
            LocalTime.MIN, LocalTime.MAX, LocalTime.of(5, 5)
        );
        when(line.dispatchTime(upStation)).thenReturn(expect);
        TransferLine transferLine = new TransferLine();
        transferLine.add(new Section(line, upStation, null, 0, 0));

        // Then, When
        assertThat(transferLine.firstDispatchTime()).isEqualTo(expect);
    }
}
