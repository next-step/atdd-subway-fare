package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.map.SubwayDispatchTime;

@DisplayName("Sections 테스트")
public class SectionsTest {
    @CsvSource(value = {
        "10:05 -> 10:20"
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 운행 시간 이내")
    @ParameterizedTest
    void arrivalTimeCase1(LocalTime startTime, LocalTime expectedTime) {
        Sections sections = createSingleLineSections();
        assertThat(sections.arrivalTime(today(startTime)))
            .isEqualTo(today(expectedTime));
    }

    @CsvSource(value = {
        "02:00 -> 05:10",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 출발 시각이 첫차 시간 이전")
    @ParameterizedTest
    void arrivalTimeCase2(LocalTime startTime, LocalTime expectedTime) {
        Sections sections = createSingleLineSections();
        assertThat(sections.arrivalTime(today(startTime)))
            .isEqualTo(today(expectedTime));
    }

    @CsvSource(value = {
        "23:30 -> 05:10",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 도착 시각이 막차 시간을 지남")
    @ParameterizedTest
    void arrivalTimeCase3(LocalTime startTime, LocalTime expectedTime) {
        Sections sections = createSingleLineSections();

        LocalDateTime expectedDateTime = LocalDateTime.of(
            LocalDate.now().plusDays(1), expectedTime
        );
        assertThat(sections.arrivalTime(today(startTime)))
            .isEqualTo(expectedDateTime);
    }

    @CsvSource(value = {
        "10:00 -> 10:24"
    }, delimiterString = " -> ")
    @DisplayName("서로 다른 노선을 가지고 있는 지하철역으로만 이루어진 Sections의 도착 시간 찾기 - 운행 시간 이내")
    @ParameterizedTest
    void arrivalTimeCase4(LocalTime startTime, LocalTime expectedTime) {
        Sections sections = createTwoLineSections();
        assertThat(sections.arrivalTime(today(startTime)))
            .isEqualTo(today(expectedTime));
    }

    private LocalDateTime today(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.now(), localTime);
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

    private Sections createTwoLineSections() {
        Line line1Stub = createLineStub(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 10)
        );
        Line line2Stub = createLineStub(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 20)
        );
        return new Sections(Arrays.asList(
            createSectionStub(line1Stub, 3),
            createSectionStub(line1Stub, 4),
            createSectionStub(line2Stub, 3)
        ));
    }

    private Line createLineStub(LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        Line lineStub = mock(Line.class);
        when(lineStub.getDispatchTime())
            .thenReturn(new SubwayDispatchTime(startTime, endTime, intervalTime));
        return lineStub;
    }

    private Section createSectionStub(Line line, int duration) {
        Section sectionStub = mock(Section.class);
        when(sectionStub.getLine()).thenReturn(line);
        when(sectionStub.getDuration()).thenReturn(duration);
        return sectionStub;
    }
}
