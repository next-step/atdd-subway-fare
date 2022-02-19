package nextstep.subway.unit.map;

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
import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SubwayDispatchTime;

@DisplayName("Path 테스트")
public class PathTest {
    @CsvSource(value = {
        "10:05 -> 10:20"
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Path 도착 시간 찾기 - 운행 시간 이내")
    @ParameterizedTest
    void arrivalTimeCase1(LocalTime startTime, LocalTime expectedTime) {
        Path path = createPathWithSingleLine();
        path.applyArrivalTime(today(startTime));

        assertThat(path.getArrivalTime())
            .isEqualTo(today(expectedTime));
    }

    @CsvSource(value = {
        "02:00 -> 05:10",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Path 도착 시간 찾기 - 출발 시각이 첫차 시간 이전")
    @ParameterizedTest
    void arrivalTimeCase2(LocalTime startTime, LocalTime expectedTime) {
        Path path = createPathWithSingleLine();
        path.applyArrivalTime(today(startTime));

        assertThat(path.getArrivalTime())
            .isEqualTo(today(expectedTime));
    }

    @CsvSource(value = {
        "23:30 -> 05:10",
    }, delimiterString = " -> ")
    @DisplayName("같은 노선의 지하철역으로만 이루어진 Path 도착 시간 찾기 - 도착 시각이 막차 시간을 지남")
    @ParameterizedTest
    void arrivalTimeCase3(LocalTime startTime, LocalTime expectedTime) {
        Path path = createPathWithSingleLine();
        path.applyArrivalTime(today(startTime));

        LocalDateTime expectedDateTime = LocalDateTime.of(
            LocalDate.now().plusDays(1), expectedTime
        );
        assertThat(path.getArrivalTime())
            .isEqualTo(expectedDateTime);
    }

    @CsvSource(value = {
        "10:00 -> 10:30"
    }, delimiterString = " -> ")
    @DisplayName("서로 다른 노선을 가지고 있는 지하철역으로만 이루어진 Path 도착 시간 찾기 - 운행 시간 이내")
    @ParameterizedTest
    void arrivalTimeCase4(LocalTime startTime, LocalTime expectedTime) {
        // 환승할 노선의 2번째 지하철 역에 탑승한다.
        // 환승할 노선의 모든 역은 10분의 간격으로 지하철이 운행된다.
        // 10시 2분 -> 10시 20분 -> 10시 30분
        Path path = createPathWithManyLine(2);
        path.applyArrivalTime(today(startTime));

        assertThat(path.getArrivalTime())
            .isEqualTo(today(expectedTime));
    }

    private LocalDateTime today(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.now(), localTime);
    }

    private Path createPathWithSingleLine() {
        Line lineStub = createLine(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 10)
        );
        Station 라인1_첫번째_역 = new Station("라인1-첫번째역");
        Station 라인1_두번째_역 = new Station("라인1-두번째역");
        Station 라인1_세번째_역 = new Station("라인1-세번째역");
        Station 라인1_네번째_역 = new Station("라인1-네번째역");
        Sections sections = new Sections(Arrays.asList(
            new Section(lineStub, 라인1_첫번째_역, 라인1_두번째_역, 1, 3),
            new Section(lineStub, 라인1_두번째_역, 라인1_세번째_역, 1, 4),
            new Section(lineStub, 라인1_세번째_역, 라인1_네번째_역, 1, 3)
        ));
        return new Path(sections);
    }

    private Path createPathWithManyLine(int subwayIndexForTransfer) {
        Station 라인1_첫번째_역 = new Station("라인1-첫번째역");
        Station 라인2_첫번째_역 = new Station("라인2-첫번째역");
        Station 라인2_두번째_역 = new Station("라인2-두번째역");
        Station 라인2_세번째_역 = new Station("라인2-세번째역");

        Line line1Stub = createLine(
            LocalTime.of(5, 0),
            LocalTime.of(23, 0),
            LocalTime.of(0, 10)
        );
        line1Stub.addSection(라인1_첫번째_역, 라인2_두번째_역, 1, 10);

        Line line2Stub = createLine(
            LocalTime.of(5, subwayIndexForTransfer * 10),
            LocalTime.of(23, 0),
            LocalTime.of(0, 20)
        );
        line2Stub.addSection(라인1_첫번째_역, 라인2_두번째_역, 1, 3);
        Sections sections = new Sections(Arrays.asList(
            new Section(line1Stub, 라인2_첫번째_역, 라인2_두번째_역, 1, 10),
            new Section(line2Stub, 라인2_두번째_역, 라인2_세번째_역, 1, 10)
        ));
        return new Path(sections);
    }

    private Line createLine(LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        Line lineStub = mock(Line.class);
        when(lineStub.dispatchTime(any()))
            .thenReturn(new SubwayDispatchTime(startTime, endTime, intervalTime));
        return lineStub;
    }
}
