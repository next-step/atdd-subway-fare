package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

@DisplayName("Sections 테스트")
public class SectionsTest {
    @CsvSource(value = {
        "2022-02-16T10:00 -> 2022-02-16T10:10"
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 운행 시간 이내")
    @ParameterizedTest
    void arrivalTimeCase1(LocalDateTime startTime, LocalDateTime expectedTime) {
        Sections sections = createSingleLineSections();
        assertThat(sections.arrivalTime(startTime)).isEqualTo(expectedTime);
    }

    @CsvSource(value = {
        "2022-02-16T02:00 -> 2022-02-16T05:10",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 출발 시각이 첫차 시간 이전")
    @ParameterizedTest
    void arrivalTimeCase2(LocalDateTime startTime, LocalDateTime expectedTime) {
        Sections sections = createSingleLineSections();
        assertThat(sections.arrivalTime(startTime)).isEqualTo(expectedTime);
    }

    @CsvSource(value = {
        "2022-02-16T11:30 -> 2022-02-17T05:13",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 도착 시각이 막차 시간을 지남")
    @ParameterizedTest
    void arrivalTimeCase3(LocalDateTime startTime, LocalDateTime expectedTime) {
        Sections sections = createSingleLineSections();
        assertThat(sections.arrivalTime(startTime)).isEqualTo(expectedTime);
    }

    private Sections createSingleLineSections() {
        Line lineStub = createLineStub(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 10)
        );
        return new Sections(Arrays.asList(
            createSectionStub(lineStub, 3),
            createSectionStub(lineStub, 4),
            createSectionStub(lineStub, 3)
        ));
    }

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
}
