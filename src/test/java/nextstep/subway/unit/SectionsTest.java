package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

@DisplayName("Sections 테스트")
public class SectionsTest {
    private Line createLineStub(LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        Line lineStub = mock(Line.class);
        when(lineStub.getStartTime()).thenReturn(startTime);
        when(lineStub.getEndTime()).thenReturn(endTime);
        when(lineStub.getIntervalTime()).thenReturn(intervalTime);
        return lineStub;
    }

    private Section createSectionStub(Line line, int duration) {
        Section sectionStub = mock(Section.class);
        when(sectionStub.getLine()).thenReturn(line);
        when(sectionStub.getDuration()).thenReturn(duration);
        return sectionStub;
    }

    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기")
    @Test
    void arrivalTime() {
        // Given
        Line lineStub = createLineStub(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 10)
        );
        Sections sections = new Sections(Arrays.asList(
            createSectionStub(lineStub, 3),
            createSectionStub(lineStub, 4),
            createSectionStub(lineStub, 3)
        ));

        // When
        final LocalTime START_TIME = LocalTime.of(10, 0);
        LocalDateTime arrivalTime = sections.arrivalTime(
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(10, 0))
        );

        // Then
        final LocalDateTime EXPECTED_TIME = LocalDateTime.of(
            LocalDateTime.now().toLocalDate(), LocalTime.of(10, 10)
        );
        assertThat(arrivalTime).isEqualTo(EXPECTED_TIME);
    }
}
